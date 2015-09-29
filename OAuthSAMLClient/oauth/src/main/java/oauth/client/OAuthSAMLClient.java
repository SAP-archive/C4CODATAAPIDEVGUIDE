package main.java.oauth.client;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.UUID;
import java.util.logging.Logger;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.apache.xml.security.Init;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Base64;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.utils.ElementProxy;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class OAuthSAMLClient {

	private static final Logger LOG = Logger
			.getLogger(OAuthSAMLClient.class.getName());
	
	private static final String KEY_STORE_TYPE = "JKS";

	private static final String SUBJECT = "saml2:Subject";
	private static final String ASSERTION = "saml2:Assertion";

	public String getAccessToken() throws IOException {

		final Form form = new Form();
		form.param("client_id", Utils.getCliendId());
		form.param("grant_type",
				"urn:ietf:params:oauth:grant-type:saml2-bearer");
		form.param("scope", "UIWC:CC_HOME");
		form.param("assertion", generateSAMLAssetion());

		Client oauthClient = ClientBuilder.newClient();

		final Response response = oauthClient
				.target(Utils.getTokenServiceUrl())
				.request()
				.header("Authorization",
						"Basic "
								+ Base64.encode((Utils.getCliendId() + ":" + Utils
										.getClientSecret()).getBytes()))
				.header("Content-Type",
						"application/x-www-form-urlencoded;charset=UTF-8")
				.post(Entity.entity(form,
						MediaType.APPLICATION_FORM_URLENCODED_TYPE));

		LOG.info("Response => " + response.getStatus() + " / "
				+ response.getStatusInfo().getReasonPhrase());

		if (response.getStatus() != Response.Status.OK.getStatusCode()) {
			throw new InternalServerErrorException(
					"Error Occured => " + System.lineSeparator() + response.readEntity(String.class));
		}
		
		OAuthToken result = response.readEntity(OAuthToken.class);

		LOG.info("OAuth Token => " + result.getAccessToken());

		return result.getAccessToken();
	}

	public String generateSAMLAssetion() throws IOException {
		String result = null;
		String refId = Utils.getRefId();

		String samlAssertion = Utils.getAssertionTemplate();

		samlAssertion = samlAssertion.replaceAll("@ISSUER", Utils.getIssuer());
		samlAssertion = samlAssertion.replaceAll("@NAME_ID", Utils.getNameId());
		samlAssertion = samlAssertion.replaceAll("@REF_ID", refId);
		samlAssertion = samlAssertion.replaceAll("@CLIENT_ID",
				Utils.getCliendId());
		samlAssertion = samlAssertion.replaceAll("@ISSUE_INSTANT",
				Utils.getDateAndTime());
		samlAssertion = samlAssertion.replaceAll("@NOT_BEFORE",
				Utils.getNotBeforeDateAndTime());
		samlAssertion = samlAssertion.replaceAll("@NOT_AFTER",
				Utils.getNotOnOrAfterDateAndTime());
		samlAssertion = samlAssertion.replaceAll("@SP_ENTITY_ID",
				Utils.getEntityId());
		samlAssertion = samlAssertion.replaceAll("@TOKEN_SERVICE_URL",
				Utils.getTokenServiceUrl());

		try {
			String filePath = OAuthSAMLClient.class.getResource("venkyvb.jks")
					.getPath();

			ByteArrayOutputStream baos = signAssertion(
					refId,
					IOUtils.toInputStream(samlAssertion, Utils.getUtf8Charset()),
					new File(filePath));
			result = new String(Base64.encode(baos.toByteArray()));
			LOG.fine("Base64 encoded assertion => " + result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static ByteArrayOutputStream signAssertion(String refId,
			InputStream xmlFile, File privateKeyFile) throws Exception {

		final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);

		final Document doc = dbf.newDocumentBuilder().parse(xmlFile);

		final NodeList subjectNodeList = doc.getElementsByTagName(SUBJECT);
		Element subjectElement = null;

		for (int i = 0; i < subjectNodeList.getLength(); i++) {
			Node node = subjectNodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				subjectElement = (Element) node;
			}
		}

		final NodeList assertionNodeList = doc.getElementsByTagName(ASSERTION);
		Element assertionElement = null;

		for (int i = 0; i < assertionNodeList.getLength(); i++) {
			Node node = assertionNodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				assertionElement = (Element) node;
				assertionElement.setIdAttribute("ID", true);
			}
		}

		doc.normalizeDocument();

		Init.init();
		ElementProxy.setDefaultPrefix(Constants.SignatureSpecNS, "ds");
		final KeyStore keyStore = loadKeyStore(privateKeyFile);
		final XMLSignature sig = new XMLSignature(doc, null,
				XMLSignature.ALGO_ID_SIGNATURE_RSA,
				Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
		final Transforms transforms = new Transforms(doc);
		transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
		transforms.addTransform(Transforms.TRANSFORM_C14N_EXCL_OMIT_COMMENTS);
		sig.addDocument("#" + refId, transforms, Constants.ALGO_ID_DIGEST_SHA1);
		final Key privateKey = keyStore.getKey(Utils.getPrivateKeyAlias(),
				Utils.getPrivateKeyPassword().toCharArray());
		final X509Certificate cert = (X509Certificate) keyStore
				.getCertificate(Utils.getPrivateKeyAlias());
		sig.addKeyInfo(cert);
		sig.addKeyInfo(cert.getPublicKey());
		sig.sign(privateKey);
		assertionElement.insertBefore(sig.getElement(), subjectElement);
		// assertionElement.appendChild(sig.getElement());

		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		Canonicalizer c14n = Canonicalizer
				.getInstance(Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
		outputStream.write(c14n.canonicalizeSubtree(doc));

		return outputStream;
	}

	private static KeyStore loadKeyStore(File privateKeyFile) throws Exception {
		final InputStream fileInputStream = new FileInputStream(privateKeyFile);
		try {
			final KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE);
			keyStore.load(fileInputStream, Utils.getKeyStorePassword()
					.toCharArray());
			return keyStore;
		} finally {
			IOUtils.closeQuietly(fileInputStream);
		}
	}

}
