package main.java.venkyvb.odata;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import main.java.venkyvb.odata.ticket.IssuePriority;
import main.java.venkyvb.odata.ticket.Note;
import main.java.venkyvb.odata.ticket.NoteType;
import main.java.venkyvb.odata.ticket.Status;
import main.java.venkyvb.odata.ticket.Ticket;

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
import org.apache.olingo.odata2.api.edm.EdmEntityContainer;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderException;
import org.apache.olingo.odata2.api.ep.EntityProviderReadProperties;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataDeltaFeed;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.apache.olingo.odata2.api.exception.ODataException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ServiceTicketODataConsumer {

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
	public static final String C4C_TENANT = "C4C_TENANT";

	private static final Logger logger = Logger
			.getLogger(ServiceTicketODataConsumer.class.getName());

	private String boundary = "batch_" + UUID.randomUUID().toString();

	private HttpClient m_httpClient = null;
	private Edm m_edm = null;
	private String m_csrfToken = null;

	private Edm readEdm() throws EntityProviderException,
			IllegalStateException, IOException {

		// This is used for both setting the Edm and CSRF Token :)
		if (m_edm != null) {
			return m_edm;
		}

		String serviceUrl = new StringBuilder(getODataServiceUrl())
				.append(SEPARATOR).append(METADATA).toString();

		logger.info("Metadata url => " + serviceUrl);

		final HttpGet get = new HttpGet(serviceUrl);
		get.setHeader(AUTHORIZATION_HEADER, getAuthorizationHeader());
		get.setHeader(CSRF_TOKEN_HEADER, CSRF_TOKEN_FETCH);

		HttpResponse response = getHttpClient().execute(get);

		m_csrfToken = response.getFirstHeader(CSRF_TOKEN_HEADER).getValue();
		logger.info("CSRF token => " + m_csrfToken);

		m_edm = EntityProvider.readMetadata(response.getEntity().getContent(),
				false);
		return m_edm;
	}

	private InputStream executeGet(String absoluteUrl, String contentType)
			throws IllegalStateException, IOException {
		final HttpGet get = new HttpGet(absoluteUrl);
		get.setHeader(AUTHORIZATION_HEADER, getAuthorizationHeader());
		get.setHeader(HTTP_HEADER_ACCEPT, contentType);

		HttpResponse response = getHttpClient().execute(get);
		return response.getEntity().getContent();
	}
	
	public ODataFeed readFeed(String serviceUri, String contentType,
			String entitySetName, SystemQueryOptions options)
			throws IllegalStateException, IOException, EntityProviderException,
			EdmException {
		Edm edm = readEdm();
		EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();
		String absolutUri = createUri(serviceUri, entitySetName, null, options);

		InputStream content = executeGet(absolutUri, contentType);
		return EntityProvider.readFeed(contentType,
				entityContainer.getEntitySet(entitySetName), content,
				EntityProviderReadProperties.init().build());
	}

	public ODataEntry readEntry(String serviceUri, String contentType,
			String entitySetName, String keyValue, SystemQueryOptions options)
			throws IllegalStateException, IOException, EdmException,
			EntityProviderException {
		EdmEntityContainer entityContainer = readEdm()
				.getDefaultEntityContainer();
		logger.info("Entity container is => " + entityContainer.getName());
		String absolutUri = createUri(serviceUri, entitySetName, keyValue,
				options);

		InputStream content = executeGet(absolutUri, contentType);

		return EntityProvider.readEntry(contentType,
				entityContainer.getEntitySet(entitySetName), content,
				EntityProviderReadProperties.init().build());
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
			e.printStackTrace();
		}

		return m_csrfToken;
	}

	public Optional<String> createTicket(Ticket ticket) throws EntityProviderException,
			IOException, Exception {

		List<BatchPart> batchParts = new ArrayList<BatchPart>();

		BatchChangeSet changeSet = BatchChangeSet.newBuilder().build();
		String contentId = UUID.randomUUID().toString();

		Map<String, String> changeSetHeaders = new HashMap<String, String>();
		changeSetHeaders.put(HTTP_HEADER_CONTENT_TYPE, "application/json");
		changeSetHeaders.put("Content-ID", contentId);
		changeSetHeaders.put("Accept", APPLICATION_JSON);

		String uriTicket = new StringBuilder("ServiceRequestCollection")
				.toString();

		BatchChangeSetPart changeRequestTicket = BatchChangeSetPart
				.method(HTTP_METHOD_POST).uri(uriTicket)
				.body(serializeTicketDeepInsert(ticket))
				.headers(changeSetHeaders).contentId(contentId).build();

		changeSet.add(changeRequestTicket);

		batchParts.add(changeSet);

		InputStream body = EntityProvider.writeBatchRequest(batchParts,
				boundary);

		String payload = IOUtils.toString(body);

		logger.info("$batch request : ");
		logger.info(payload);

		String serviceUrl = getODataServiceUrl();

		HttpResponse batchResponse = executeBatchCall(serviceUrl, payload);

		logger.info("$batch response getStatusCode => "
				+ batchResponse.getStatusLine().getStatusCode());

		for (Header h : batchResponse.getAllHeaders()) {
			logger.info(h.getName() + ":" + h.getValue());
		}

		InputStream responseBody = batchResponse.getEntity().getContent();
		String contentType = batchResponse.getFirstHeader(
				HttpHeaders.CONTENT_TYPE).getValue();
		
		String response = IOUtils.toString(responseBody);

		logger.info("$batch response :");
		logger.info(response);
		
		// Process the batch response to get the ticket key
		List<BatchSingleResponse> responses = EntityProvider
				.parseBatchResponse(IOUtils.toInputStream(response),
						contentType);
		for (BatchSingleResponse rsp : responses) {
			// Look for only created entries
			logger.info("Single Response status code => " + rsp.getStatusCode());
			if (Integer.parseInt(rsp.getStatusCode()) == 201 ) { // 201 - HttpStatus code created
				// Http POST call returns the link to the new resource via the location header
				// Format is https://<service_url>/<Collection>('key')
				String locationUrl = rsp.getHeader("location");
				if (!StringUtils.isBlank(locationUrl)) {
					String ticketUUID = StringUtils.substringBetween(locationUrl,
							"'");
					return Optional.of(ticketUUID);
				}
			}
		}
		
		return Optional.empty();
	}

	public void updateTicket(Ticket ticket, Note note)
			throws EntityProviderException, IOException, Exception {
		// Update by $batch changeSets..
		List<BatchPart> batchParts = new ArrayList<BatchPart>();
		String contentId = UUID.randomUUID().toString();

		BatchChangeSet changeSet = BatchChangeSet.newBuilder().build();

		Map<String, String> changeSetHeaders = new HashMap<String, String>();
		changeSetHeaders.put(HTTP_HEADER_CONTENT_TYPE, "application/json");
		changeSetHeaders.put("Content-ID", contentId);
		changeSetHeaders.put("Accept", APPLICATION_JSON);

		String uriTicket = new StringBuilder("ServiceRequestCollection('")
				.append(ticket.getTicketUUID()).append("')").toString();

		BatchChangeSetPart changeRequestTicket = BatchChangeSetPart
				.method(HTTP_METHOD_PATCH).uri(uriTicket)
				.body(serializeTicket(ticket)).headers(changeSetHeaders)
				.build();

		changeSet.add(changeRequestTicket);

		String uriNotes = new StringBuilder("ServiceRequestNotesCollection")
				.toString();

		changeSetHeaders.put("Content-ID", UUID.randomUUID().toString());
		BatchChangeSetPart changeRequestNote = BatchChangeSetPart
				.method(HTTP_METHOD_POST).uri(uriNotes)
				.body(serializeNote(ticket, note)).headers(changeSetHeaders)
				.build();

		changeSet.add(changeRequestNote);
		batchParts.add(changeSet);

		InputStream body = EntityProvider.writeBatchRequest(batchParts,
				boundary);

		String payload = IOUtils.toString(body);

		logger.info("$batch request : ");
		logger.info(payload);

		String serviceUrl = getODataServiceUrl();

		HttpResponse batchResponse = executeBatchCall(serviceUrl, payload);

		logger.info("$batch response getStatusCode => "
				+ batchResponse.getStatusLine().getStatusCode());

		for (Header h : batchResponse.getAllHeaders()) {
			logger.info(h.getName() + ":" + h.getValue());
		}

		InputStream responseBody = batchResponse.getEntity().getContent();
		String contentType = batchResponse.getFirstHeader(
				HttpHeaders.CONTENT_TYPE).getValue();

		logger.info("$batch response :");
		logger.info(IOUtils.toString(responseBody));
	}

	private HttpResponse executeBatchCall(String serviceUrl, final String body)
			throws ClientProtocolException, IOException {
		final HttpPost post = new HttpPost(URI.create(serviceUrl + "/$batch"));
		post.setHeader("Content-Type", "multipart/mixed;boundary=" + boundary);
		post.setHeader(AUTHORIZATION_HEADER, getAuthorizationHeader());
		post.setHeader(CSRF_TOKEN_HEADER, getCsrfToken());
		HttpEntity entity = new StringEntity(body);

		post.setEntity(entity);

		for (Header h : post.getAllHeaders()) {
			logger.info(h.getName() + " : " + h.getValue());
		}

		HttpResponse response = getHttpClient().execute(post);

		logger.info("Response statusCode => "
				+ response.getStatusLine().getStatusCode());

		return response;
	}

	private HttpClient getHttpClient() {
		if (this.m_httpClient == null) {
			this.m_httpClient = HttpClientBuilder.create().build();
		}
		return this.m_httpClient;
	}

	public String serializeTicketDeepInsert(Ticket t)
			throws EntityProviderException, IOException, Exception {

		// Input fields Priority, ProductID, IssueCategory, Subject & customer
		// note
		Map<String, Object> prop = new HashMap<String, Object>();

		if (t.getIssuePriority() != null) {
			prop.put("Priority", t.getIssuePriority().getPriorityCode());
		}

		if (!StringUtils.isBlank(t.getProductId())) {
			prop.put("ProductID", t.getProductId());
		}

		if (!StringUtils.isBlank(t.getSubject())) {
			prop.put("Subject", t.getSubject());
		}

		if (!StringUtils.isBlank(t.getIssueCategory())) {
			prop.put("ServiceIssueCategoryID", t.getIssueCategory());
		}
		
		if(!StringUtils.isBlank(t.getAccountId())){
			prop.put("AccountID", t.getAccountId());
		}
		
		if(!StringUtils.isBlank(t.getContactId())){
			prop.put("ContactID", t.getContactId());
		}

		Map<String, Object> propNotes = new HashMap<String, Object>();
		if (t.getNotes() != null) {
			propNotes.put("Text", t.getNotes().get(0).getNoteDescription());
			propNotes.put("TypeCode", t.getNotes().get(0).getNoteType().getTypeCode()); // Incident description
			ArrayList<Map<String, Object>> arrayMap = new ArrayList<Map<String, Object>>();
			arrayMap.add(propNotes);
			prop.put("ServiceRequestNotes", arrayMap);
		}

		ObjectMapper mapper = new ObjectMapper();

		return mapper.writeValueAsString(prop);
	}

	public String serializeTicket(Ticket t) throws EntityProviderException,
			IOException, Exception {

		// Input fields Priority, ProductID, IssueCategory, Subject & customer
		// note
		Map<String, Object> prop = new HashMap<String, Object>();

		if (t.getIssuePriority() != null) {
			prop.put("Priority", t.getIssuePriority().getPriorityCode());
		}

		if (!StringUtils.isBlank(t.getProductId())) {
			prop.put("ProductID", t.getProductId());
		}

		if (!StringUtils.isBlank(t.getSubject())) {
			prop.put("Subject", t.getSubject());
		}

		if (!StringUtils.isBlank(t.getIssueCategory())) {
			prop.put("ServiceIssueCategoryID", t.getIssueCategory());
		}

		ObjectMapper mapper = new ObjectMapper();

		return mapper.writeValueAsString(prop);
	}

	public String serializeNote(Ticket ticket, Note note)
			throws EntityProviderException, IOException, Exception {

		Map<String, Object> prop = new HashMap<String, Object>();

		prop.put("ParentObjectID", ticket.getTicketUUID());
		prop.put("Text", note.getNoteDescription());
		prop.put("TypeCode", note.getNoteType().getTypeCode());

		ObjectMapper mapper = new ObjectMapper();

		return mapper.writeValueAsString(prop);
	}

	public ArrayList<Ticket> readTickets() throws Exception {

		ArrayList<Ticket> result = new ArrayList<Ticket>();

		String serviceUrl = getODataServiceUrl();

		SystemQueryOptions queryOptions = this.new SystemQueryOptions();
		String queryString = null;
		queryString = "?$format=json&$orderby=LastChangeDateTime%20desc&$top=10";

		queryOptions.setQueryCondition(queryString);

		ODataFeed feed = readFeed(serviceUrl, APPLICATION_JSON,
				"ServiceRequestCollection", queryOptions);

		logger.info("Read: " + feed.getEntries().size() + " entries");
		for (ODataEntry entry : feed.getEntries()) {
			result.add(mapEntryToTicket(entry));
		}

		return result;
	}

	public Ticket readTicketById(String ticketId) throws IOException,
			ODataException {

		List<Ticket> tickets = new ArrayList<Ticket>();

		String serviceUrl = getODataServiceUrl();

		SystemQueryOptions queryOptions = this.new SystemQueryOptions();
		String queryString = new StringBuffer(
				"?$format=json&$expand=ServiceRequestNotes&$filter=ObjectID%20eq%20%27")
				.append(ticketId).append("%27").toString();
		queryOptions.setQueryCondition(queryString);

		ODataFeed feed = readFeed(serviceUrl, APPLICATION_JSON,
				"ServiceRequestCollection", queryOptions);

		logger.info("Read: " + feed.getEntries().size() + " entries");

		for (ODataEntry entry : feed.getEntries()) {
			tickets.add(mapEntryToTicket(entry));
		}

		if (tickets.size() > 1) {
			// Should be an error !!
		}

		Ticket result = tickets.get(0);

		return tickets.get(0);
	}

	public Ticket mapEntryToTicket(ODataEntry entry) {

		Ticket result = new Ticket();

		Map<String, Object> propMap = entry.getProperties();

		for (Map.Entry<String, Object> e : propMap.entrySet()) {
			Object value = e.getValue();
			String propName = e.getKey();

			if (value instanceof ODataDeltaFeed
					&& propName.equals("ServiceRequestNotes")) {
				ODataDeltaFeed deltaFeed = (ODataDeltaFeed) value;
				result.setNotes(mapNote(deltaFeed));
			} else if (value instanceof Calendar) {
				Calendar cal = (Calendar) value;
				if (propName.equals("CreationDateTime")) {
					result.setCreatedAt(cal.getTime());
				}
				if (propName.equals("LastChangeDateTime")) {
					result.setChangedAt(cal.getTime());
				}
			} else {
				// Handle ticket attributes
				if (value != null) {
					if (propName.equals("Status")) {
						result.setStatus(Status
								.fromStatusCode(value.toString()).get());
					}

					if (propName.equals("ServiceIssueCategoryID")
							&& value != null) {
						result.setIssueCategory(value.toString().toUpperCase());
					}

					if (propName.equals("Subject")) {
						result.setSubject(value.toString());
					}

					if (propName.equals("ProductID")) {
						result.setProductId(value.toString());
					}

					if (propName.equals("ObjectID")) {
						result.setTicketUUID(value.toString());
					}

					if (propName.equals("ID")) {
						// Remove leading zeros for the ticket ID..
						result.setTicketId(value.toString().replaceFirst(
								"^0+(?!$)", ""));
					}

					if (propName.equals("Priority")) {
						result.setIssuePriority(IssuePriority.fromPriorityCode(
								value.toString()).get());
					}

					if (propName.equals("AccountID")) {
						result.setAccountId(value.toString());
					}

					if (propName.equals("ContactID")) {
						result.setContactId(value.toString());
					}
				}
			}

		}

		return result;
	}

	public ArrayList<Note> mapNote(ODataDeltaFeed feed) {
		ArrayList<Note> result = new ArrayList<Note>();
		List<ODataEntry> entries = feed.getEntries();

		for (ODataEntry entry : entries) {
			Note note = new Note();

			for (Map.Entry<String, Object> e : entry.getProperties().entrySet()) {
				Object value = e.getValue();
				String propName = e.getKey();

				if (value instanceof Calendar) {
					Calendar cal = (Calendar) value;
					if (propName.equals("UpdatedOn")) {
						note.setCreatedAt(cal.getTime());
					}
				} else {
					// Handle ticket attributes
					if (value != null) {
						if (propName.equals("Text")) {
							note.setNoteDescription(value.toString());
						}

						if (propName.equals("TypeCode")) {
							note.setNoteType(NoteType.fromTypeCode(value.toString()).get());
						}
					}
				}
			}

			result.add(note);
		}

		return result;
	}

	private String createUri(String serviceUri, String entitySetName,
			String id, SystemQueryOptions options) {

		final StringBuilder absolauteUri = new StringBuilder(serviceUri)
				.append(SEPARATOR).append(entitySetName);
		if (id != null) {
			absolauteUri.append("('").append(id).append("')");
		}

		if (options != null) {
			if (options.getQueryCondition() != null) {
				absolauteUri.append(options.getQueryCondition());
			}
		}

		logger.info("createUri : " + absolauteUri.toString());
		return absolauteUri.toString();
	}

	public class SystemQueryOptions {
		private String queryCondition;

		public String getQueryCondition() {
			return queryCondition;
		}

		public void setQueryCondition(String queryCondition) {
			this.queryCondition = queryCondition;
		}
	}

	private String getODataServiceUrl() {
		return "https://HOST_NAME/sap/byd/odata/v1/SERVICE_NAME";
	}

	private String getAuthorizationHeader() {
		// Note: This example uses Basic Authentication
		// Preferred option is to use OAuth SAML bearer flow.
		String temp = new StringBuilder("USER_NAME").append(":")
				.append("PASSWORD").toString();
		String result = "Basic "
				+ new String(Base64.encodeBase64(temp.getBytes()));
		logger.info("AuthorizationHeader " + result);
		return result;
	}

}
