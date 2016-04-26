package c4c.odata;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.olingo.odata2.api.client.batch.BatchChangeSet;
import org.apache.olingo.odata2.api.client.batch.BatchChangeSetPart;
import org.apache.olingo.odata2.api.client.batch.BatchPart;
import org.apache.olingo.odata2.api.client.batch.BatchSingleResponse;
import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AccountAttachmentODataConsumer {

	public static final String HTTP_METHOD_PUT = "PUT";
	public static final String HTTP_METHOD_POST = "POST";
	public static final String HTTP_METHOD_GET = "GET";
	public static final String HTTP_METHOD_PATCH = "PATCH";
	private static final String HTTP_METHOD_DELETE = "DELETE";

	public static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
	public static final String HTTP_HEADER_ACCEPT = "Accept";

	public static final String APPLICATION_JSON = "application/json";
	public static final String APPLICATION_XML = "application/xml";
	public static final String APPLICATION_ATOM_XML = "application/atom+xml";
	public static final String METADATA = "$metadata";
	public static final String SEPARATOR = "/";
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String CSRF_TOKEN_HEADER = "X-CSRF-Token";
	public static final String CSRF_TOKEN_FETCH = "Fetch";
	public static final String PROPERTIES_FILE = "settings.properties";
	
	public static final String C4C_TENANT = "C4C_TENANT";

	private static final Logger logger = Logger
			.getLogger(AccountAttachmentODataConsumer.class.getName());

	private String boundary = "batch_" + UUID.randomUUID().toString();

	private HttpClient m_httpClient = null;
	private Edm m_edm = null;
	private String m_csrfToken = null;

	private static String username;
	private static String password;
	private static String ODataServiceURL;
	private static String attachmentsFolder;
	
	static {
		logger.setLevel(Level.SEVERE);
	}

	private Edm readEdm() throws EntityProviderException,
			IllegalStateException, IOException {

		// This is used for both setting the Edm and CSRF Token :)
		if (m_edm != null) {
			return m_edm;
		}

		String serviceUrl = new StringBuilder(getODataServiceURL())
				.append(SEPARATOR).append(METADATA).toString();

		logger.info("Metadata url => " + serviceUrl);

		final HttpGet get = new HttpGet(serviceUrl);
		get.setHeader(AUTHORIZATION_HEADER, getAuthorizationHeader());
		get.setHeader(CSRF_TOKEN_HEADER, CSRF_TOKEN_FETCH);

		HttpResponse response = getHttpClient().execute(get);
		if(response.getStatusLine().getStatusCode() != 200) {
			logger.severe(response.getStatusLine().toString());
		}

		m_csrfToken = response.getFirstHeader(CSRF_TOKEN_HEADER).getValue();
		logger.info("CSRF token => " + m_csrfToken);

		m_edm = EntityProvider.readMetadata(response.getEntity().getContent(),
				false);
		return m_edm;
	}

	public String getCsrfToken() {

		if (m_csrfToken != null) {
			return m_csrfToken;
		}

		// Force a server call to fetch the EDM again
		m_edm = null;
		try {
			readEdm();
		} catch (EntityProviderException | IllegalStateException | IOException e) {
			logger.severe(e.getMessage());
			e.printStackTrace();
		}

		return m_csrfToken;
	}

	private HttpResponse executeBatchCall(String serviceUrl, final String body)
			throws ClientProtocolException, IOException {
		final HttpPost post = new HttpPost(URI.create(serviceUrl + "/$batch"));
		post.setHeader("Content-Type", "multipart/mixed;boundary=" + boundary);
		post.setHeader(AUTHORIZATION_HEADER, getAuthorizationHeader());
		post.setHeader(CSRF_TOKEN_HEADER, getCsrfToken());

		HttpEntity entity = new StringEntity(body);

		post.setEntity(entity);

		String logText = "REQUEST HEADERS:\n";
		for (Header h : post.getAllHeaders()) {
			logText = logText + h.getName() + " : " + h.getValue() + "\n";
		}
		logger.info(logText);

		HttpResponse response = getHttpClient().execute(post);

		logger.info("$Batch Response statusCode => "
				+ response.getStatusLine().getStatusCode());

		return response;
	}

	private HttpClient getHttpClient() {
		if (this.m_httpClient == null) {
			this.m_httpClient = HttpClientBuilder.create().build();
		}
		return this.m_httpClient;
	}

	public Optional<String> createAttachment(Attachment attachment)
			throws EntityProviderException, IOException, Exception {

		List<BatchPart> batchParts = new ArrayList<BatchPart>();

		BatchChangeSet changeSet = BatchChangeSet.newBuilder().build();
		String contentId = UUID.randomUUID().toString();

		Map<String, String> changeSetHeaders = new HashMap<String, String>();
		changeSetHeaders.put(HTTP_HEADER_CONTENT_TYPE, "application/json");
		changeSetHeaders.put("Content-ID", contentId);
		changeSetHeaders.put("Accept", APPLICATION_JSON);

		String uriAttachment = new StringBuilder(
				"AccountAttachmentCollection").toString();

		BatchChangeSetPart changeRequestAttachment = BatchChangeSetPart
				.method(HTTP_METHOD_POST).uri(uriAttachment)
				.body(serializeAttachment(attachment))
				.headers(changeSetHeaders).contentId(contentId).build();

		changeSet.add(changeRequestAttachment);

		batchParts.add(changeSet);

		InputStream body = EntityProvider.writeBatchRequest(batchParts,
				boundary);

		String payload = IOUtils.toString(body);

		// logger.info("$batch request : ");
		// logger.info(payload);

		String serviceUrl = getODataServiceURL();

		HttpResponse batchResponse = executeBatchCall(serviceUrl, payload);

		String logText = "RESPONSE HEADERS:\n";
		for (Header h : batchResponse.getAllHeaders()) {
			logText = logText + h.getName() + " : " + h.getValue() + "\n";
		}
		logger.info(logText);

		InputStream responseBody = batchResponse.getEntity().getContent();
		String contentType = batchResponse.getFirstHeader(
				HttpHeaders.CONTENT_TYPE).getValue();

		String response = IOUtils.toString(responseBody);

		// logger.info("$batch response :");
		// logger.info(response);

		// Process the batch response to get the ticket key
		List<BatchSingleResponse> responses = EntityProvider
				.parseBatchResponse(IOUtils.toInputStream(response),
						contentType);
		for (BatchSingleResponse rsp : responses) {
			// Look for only failed entries
			logger.info("Single Response status code => " + rsp.getStatusCode());
			if (Integer.parseInt(rsp.getStatusCode()) != 201) { // 201 -
																// HttpStatus
																// code created
				String retText = "Failed to create attachment for ["
						+ "ParentObjectID: " + attachment.get_ParentObjectID()
						+ ", Attachment name : " + attachment.get_Name()
						+ ", MimeType: " + attachment.get_MimeType()
						+ ", TypeCode: " + attachment.get_TypeCode()
						+ ", Filename: " + attachment.getFileName() + "] "
						+ "Error message: " + rsp.getBody();

				return Optional.of(retText);
			}
		}

		return Optional.empty();
	}

	public String serializeAttachment(Attachment a)
			throws EntityProviderException, IOException, Exception {

		// Input fields CorporateAccountExternalKey, TypeCode, Name, MimeType &
		// Binary
		Map<String, Object> prop = new HashMap<String, Object>();

		if (!StringUtils.isBlank(a.get_Name())) {
			prop.put("Name", a.get_Name());
		}

		if (!StringUtils.isBlank(a.get_ParentObjectID())) {
			prop.put("ParentObjectID", a.get_ParentObjectID());
		}

		if (!StringUtils.isBlank(a.get_TypeCode())) {
			prop.put("TypeCode", a.get_TypeCode());
		}

		if (!StringUtils.isBlank(a.get_MimeType())) {
			prop.put("MimeType", a.get_MimeType());
		}

		if (a.get_Binary() != null) {
			prop.put("Binary", a.get_Binary());
		}

		ObjectMapper mapper = new ObjectMapper();

		return mapper.writeValueAsString(prop);
	}

	private String getAuthorizationHeader() {
		// Note: This example uses Basic Authentication
		// Preferred option is to use OAuth SAML bearer flow.
		String temp = new StringBuilder(getUsername()).append(":")
				.append(getPassword()).toString();
		String result = "Basic "
				+ new String(Base64.encodeBase64(temp.getBytes()));
		logger.info("AuthorizationHeader " + result);
		return result;
	}

	public static Properties readSettings() throws IOException {
		Properties prop = new Properties();

		String path = PROPERTIES_FILE;
		
	    //load the file handle for main.properties
		FileInputStream is = new FileInputStream(path);
		
		if (is != null) {
			prop.load(is);

			// get the property value and print it out
			setODataServiceURL(prop.getProperty("ODATA_SERVICE_URL"));
			setUsername(prop.getProperty("USERNAME"));
			setAttachmentsFolder(prop.getProperty("ATTACHMENTS_FOLDER"));
		} else {
			throw new FileNotFoundException("Property file '" + path
					+ "' not found in classpath");
		}
		return prop;
	}

	public static String getUsername() {
		return username;
	}

	public static void setUsername(String username) {
		AccountAttachmentODataConsumer.username = username;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		AccountAttachmentODataConsumer.password = password;
	}

	public static String getODataServiceURL() {
		return ODataServiceURL;
	}

	public static void setODataServiceURL(String oDataServiceURL) {
		ODataServiceURL = oDataServiceURL;
	}

	public static String getAttachmentsFolder() {
		return attachmentsFolder;
	}

	public static void setAttachmentsFolder(String attachmentsFolder) {
		AccountAttachmentODataConsumer.attachmentsFolder = attachmentsFolder;
	}

}
