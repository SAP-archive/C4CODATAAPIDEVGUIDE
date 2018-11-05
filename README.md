# SAP Cloud for Customer OData API Developer's Guide
The SAP Cloud for Customer OData API Developer’s Guide complements the [SAP Cloud for Customer OData API Reference](https://help.sap.com/viewer/26fdb8fadd5b4becb5c858d92146d0e0/1708/en-US/e4d5b5e4f6d847f7ad2025f5f343e03f.html) with usage details and samples for SAP Cloud for Customer OData API in a format that is most convenient to developers. Furthermore, it also covers known restrictions and limitations of the SAP Cloud for Customer OData API.

For a brief introduction to SAP Cloud for Customer OData API, please refer to [SAP Cloud for Customer OData API Documentation](https://help.sap.com/viewer/26fdb8fadd5b4becb5c858d92146d0e0/1708/en-US/6c0a463cc9ca450cbd01a9a5057ce682.html).

## Table of Contents

<!-- MarkdownTOC -->

- [SAP Cloud for Customer OData API Developer's Guide](#sap-cloud-for-customer-odata-api-developers-guide)
    - [Table of Contents](#table-of-contents)
    - [What is OData protocol?](#what-is-odata-protocol)
        - [OData versions](#odata-versions)
    - [SAP Cloud for Customer (C4C) OData Services](#sap-cloud-for-customer-c4c-odata-services)
        - [OData Service Catalog](#odata-service-catalog)
        - [Authentication](#authentication)
        - [SAP Standard vs. Custom OData Services](#sap-standard-vs-custom-odata-services)
        - [OData Service Document](#odata-service-document)
        - [OData Service Metadata](#odata-service-metadata)
        - [Supported HTTP operations](#supported-http-operations)
        - [SAP Cloud for Customer Annotations](#sap-cloud-for-customer-annotations)
        - [Known Limitations](#known-limitations)
    - [Consuming C4C OData API](#consuming-c4c-odata-api)
        - [Supported Formats](#supported-formats)
        - [Authentication](#authentication)
        - [CSRF Token](#csrf-token)
        - [Server side paging](#server-side-paging)
        - [Sample Java Client](#sample-java-client)
        - [Supported System Query Options](#supported-system-query-options)
            - [$batch](#batch)
            - [$expand](#expand)
            - [$filter](#filter)
            - [$inlinecount](#inlinecount)
            - [$search](#search)
        - [ETag Support](#etag-support)
            - [Optimistic Concurrency Control with ETag](#optimistic-concurrency-control-with-etag)
    - [Sample Payloads](#sample-payloads)

<!-- /MarkdownTOC -->

## What is OData protocol?
[Open Data (OData) Protocol](https://www.oasis-open.org/committees/tc_home.php?wg_abbrev=odata) is an OASIS standard that defines best practices for building and consuming RESTful APIs. It is based on HTTP protocol and provides metadata for the entities it exposes and their relationships. In some ways, it is similar to SQL for a relational database system (RDBMS) as it provides querying options such as filtering, ordering results, support for pagination, number of records and more. It supports both XML (Atom) and JSON formats for querying and modifying data.

For more information on OData please refer to http://www.odata.org where you can find detailed documentation and tutorials. 

### OData versions
SAP Cloud for Customer, specifically, supports the V2.0 of the OData protocol (with some additional enhancements and a few limitations), you can read the details of OData V2 [here](http://www.odata.org/documentation/odata-version-2-0/).

## SAP Cloud for Customer (C4C) OData Services
You can try the examples shown in this document by accessing the OData API of your SAP Cloud for Customer (C4C) tenant using the following URL pattern:

```
https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/v1/odataservicecatalog/ODataServiceCollection
```

where myNNNNNN is the name of your tenant.

### OData Service Catalog
OData Service Catalog contains the list of available OData Services in the corresponding C4C tenant. In order to get the list of available OData services in your C4C tenant use the following URL:

```
https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/v1/odataservicecatalog/ODataServiceCollection
```

The catalog service returns both standard OData services delivered by SAP as well as the custom services that you may have modeled in your tenant using the [OData Service Explorer](https://help.sap.com/viewer/26fdb8fadd5b4becb5c858d92146d0e0/1708/en-US/8e4220fa6dc943ef891fb3d0e91515d3.html).

### Authentication
SAP Cloud for Customer OData API supports the following authentication mechanisms:

* Basic Authentication (username and password pair)
* OAuth SAML Bearer flow (you can find sample Java implementation of OAuth SAML bearer client [here](OAuthSAMLClient).)
* SAML Based FrontEnd SSO authentication
* X.509 Certificate authentication

Please note that the C4C system used in the example URLs throughout this document, doesn't require authentication.

### SAP Standard vs. Custom OData Services

SAP Cloud for Customer provides a standard OData API. In addition, SAP Cloud for Customer also allows customers to build their own (custom) OData services based on the predefined business objects in the solution.

[OData Service Explorer](https://help.sap.com/viewer/26fdb8fadd5b4becb5c858d92146d0e0/1708/en-US/8e4220fa6dc943ef891fb3d0e91515d3.html) is a key user tool that allows exploring and testing SAP provided OData API as well as building custom services.

Standard and custom OData services offer the same capabilities and, are subjected to the same restrictions. 

The following URL pattern differetiates the Standard and Custom OData services.


* Standard services - `https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/v1/....`
* Custom services - `https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/cust/v1/...`

### OData Service Document
OData service document contains the list of OData entities (a.k.a. collections) contained within that OData service. In order to retrieve the complete list of entity sets included in C4C OData service, you can open the following URL in your browser.

```
https://myNNNNNN.crm.ondemand/sap/c4c/odata/v1/c4codataapi/ 
```

where myNNNNNN is the name of your C4C tenant. (Please note that ‘/’ character at the end of the URI is required!)

### OData Service Metadata
OData service metadata is retrieved via the following URL.

```
https://myNNNNNN.crm.ondemand/sap/c4c/odata/v1/c4codataapi/$metadata
```

Labals for the entities and their properties can be retrieved by appending the query parameter <i>sap-label=true</i>.

```
https://myNNNNNN.crm.ondemand/sap/c4c/odata/v1/c4codataapi/$metadata?sap-label=true
```

To receive the UI labels in a particular language HTTP header Accept-Language can be used. Prefered language code can be set based on [ISO 639-1](https://en.wikipedia.org/wiki/ISO_639-1).

For example, to receive the UI labels in Turkish the HTTP request header should be set as below:

```
Accept-Language:tr 
```

### Supported HTTP operations

C4C OData API supports the following OData/HTTP operations:

Operation | Description
----------|------------
GET | Used to retrieve a single entity instance or multiple entity instances
POST | Used to create entity instances
PUT | Used to **completely** replace/overwrite and existing entity instance
PATCH | Used to replace/overwrite existing entity instance. The key difference between PUT and PATCH is that PUT overwrites the complete entity whereas PATCH only updates **only** attributes of the entity that are part of the payload
DELETE | Used to delete an entity record
$batch | Used to perform multiple query, create, update and delete operations with explicit transaction boundaries specified via Changesets as a part of the payload
Deep Insert | Used with **POST**. Allows the creation of complete entity (header entry, child entries etc) with a single POST request


### SAP Cloud for Customer Annotations

Following table describes the OData Framework behavior as of the November, 2018 release (1811).

|Annotation|Definition|Framework Behaviour|
|----------|----------|-------------------|
|sap:creatable = true|Property is relevant while creating a new record|OData Framework passes the value in the payload to the Business Object.|
|sap:creatable = false|Property is NOT relevant for creating a new record|OData Framework passes the value in the payload to the Business Object.|
|sap:updatable = true|Property is relevant while updating a record|OData Framework passes the value in the payload to the Business Object.|
|sap:updatable = false|Property is NOT relevant while updating a record|OData Framework passes the value in the payload to the Business Object.|
|sap:filterable = true|The property can be used in $filter query parameter|OData Framework passes the filter value to the Business Object.|
|sap:filterable = false|filterable = false|If a property is set to filterable=false and $filter is used on that property OData FW raises an error|

**Exception**: If a property has the attributes sap:creatable = false and Nullable = false (usually the case for ObjectID property as the value is auto-generated); OData Framework will ignore any value provided for such properties without raising an error.

**Note**: The "Nullable" attribute indicates if the underlying field in the database allows null values. It is only relevant for the API consumers when combined with sap:creatable and sap:updatable annotations. For example: A property with the combination of Nullable=false AND sap:creatable = true attributes indicates that the a value for the propety is Mandatory (required). 

### Known Limitations

|Limitation|Workaround|
|----------|----------|
| C4C OData API **DOES NOT** support usage of String, Date and Math Functions in $filter System Query Option.| See [$filter](#filter) for supported options|
|Logical OR only works for the same property. E.g. "...$filter=PartyID eq '1001' or PartyID eq '1002'" works. "...$filter=PartyID eq '1001' or TerritoryID eq 'CA'" not supported.| Each or segment can be executed as a seperate query, and the results can be collated. E.g. : 1st Query - " ...$filter=PartyID eq '1001'" 2nd Query - "$filter=TerritoryID eq 'CA'". In order to reduce round trips to the server, multiple queries can be executed as part of a $batch query.|
|C4C OData API currently **DOES NOT** support the usage of properties from expanded navigations as part of $filter conditions.|  See [$expand](#expand) for a possible workaround when the sub-entity contains a reference to the main entity with the property *ParentObjectID*.|

## Consuming C4C OData API
### Supported Formats
SAP Cloud for Customer OData API supports HTTP request and response payloads in both Atom (XML) and JSON formats. The default payload format is Atom (XML). In order to use JSON format please follow the instructions below:
* For HTTP GET requests, use the system query parameter `$format=json`. 

Example:
```
https://myNNNNNN.crm.ondemand/sap/c4c/odata/v1/odataservicecatalog/?$format=json
```

will return 

```JSON
{"d":{"EntitySets":["ODataServiceCollection"]}}
```

* For HTTP POST/PATCH/PUT requests with JSON payload, set the HTTP `Content-Type` header as below:

```
Content-Type: application/json
```

### Authentication
All HTTP requests should have an `Authorization` header. 


Authentication Method |HTTP Header
-------|---------
Basic authentication | `Authorization: Basic _base64_encoded_value_of_username:password_`
OAuth SAML bearer flow  | `Authorization: Bearer _OAuth_token_` 

In the formats shown above, please note the space between `Basic`, `Bearer` and the values following them respectively.

Please note that the C4C system used in the example URLs throughout this document, doesn't require authentication.

### CSRF Token
In order to prevent possible [Cross-site request forgery](https://en.wikipedia.org/wiki/Cross-site_request_forgery) attacks, SAP Cloud for Customer OData API requires all modifying HTTP requests (POST/PUT/PATCH) to specify a CSRF token, in addition to the Authorization header.

Please follow the steps below to receive a CSRF token:

First, perform an HTTP GET request to the service end-point (e.g. retrieve the service document end-point `https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/v1/c4codataapi/`) with the HTTP request header:

```
	x-csrf-token: fetch
```

After a successful call, the C4C server will respond with the expected response payload and a CSRF token in the respective response `X-CSRF-Token`. 

Here is an example CSRF Token returned as part of the response header 

```
	x-csrf-token: Xi6wOfG-O55Wt8ZkhYW0eA==
```

The token value retrieved above needs to be used for subsequent modifying HTTP requests (i.e. POST/PUT/PATCH).

### Server side paging
For HTTP GET requests, if no query options are specified, the server enforces paging in order to provide better performance. Currently the page size is fixed at 1000 entries. 

If there are more than 1000 entries, the server includes a `<link re"next" href="...` element that can be used to retrieve the next 1000 entries. 

Here is an excerpt with the **next** link:

```XML
...
				<d:StartDate>2012-08-25T00:00:00</d:StartDate>
				<d:StatusCode>6</d:StatusCode>
				<d:StatusCodeText>Converted</d:StatusCodeText>
				<d:UUID>00163E03-A070-1EE2-8BE6-D1A72CF7B7D6</d:UUID>
			</m:properties>
		</content>
	</entry>
	<link rel="next" href="https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/v1/c4codataapi/LeadCollection/?$skiptoken=1001"/>
</feed>
```

(in this specific case the LeadCollection entity set is being queried).


Server side paging can also be implemented for a set number of records. In this case, $top and $skip system query options can be used together retrieve a page of records. The following example returns 100 records starting from record number 301.

```
https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/v1/c4codataapi/AccountCollection?$skip=300&$top=100
```

### Sample Java Client
A sample Java client demonstrating how to make OData calls to C4C is available [here](ODataConsumerSample). The sample uses Apache Olingo library to construct and read OData payloads.

### Supported System Query Options
As stated above, SAP Cloud for Customer supports version 2 of the OData protocol. Here we list the set of system query options that are supported by the C4C OData implementation. For brevity, initial part of the URL https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/v1/c4codataapi is skipped in the following examples:

Query Option | Example | Description
-------|---------|------------
**$batch** | /$batch | Perform several OData query, create, update or delete operations with a single HTTP POST call.
**$count** | /OpportunityCollection/$count | Returns the total number of Opportunities
**$format**  | /OpportunityCollection?$format=json | Returns Opportunity entries in JSON format with server side paging
**$inlinecount** | /OpportunityCollection?$top=10&$inlinecount=allpages | Returns the top 10 opportunities and also returns the total number of opportunities.
**$orderby** | /OpportunityCollection?$orderby=CloseDate desc&$top=10 | First performs an orderby on the Opportunities and then selects the top 10 from that ordered list. Here **desc** means descending order.
**$search** | /CustomerCollection?$search='Porter' | Returns Customer entries with at least one of the search enabled fields contain the word 'Porter'
**$select** | /OpportunityCollection?$select=OpportunityID,AccountID | Returns Opportunity entries but only 2 attributes OpportunityID and AccountID
**$skip** | /OpportunityCollection?$skip=10 | Skips the first 10 entries and then returns the rest
**$top** |  /OpportunityCollection?$top=10 | Returns top 2 Opportunities. 'Top 2' is defined by server logic here

Below you will find additional details for some of the system query options.

#### $batch
Used to query, create/update multiple entities with explicit transaction boundaries specified via Changesets as a part of the payload

The following example;
- retrieves top 3 entries from the ServiceRequestCollection (of the requestservice OData service)
- updates a few properties of a ServiceRequest entry
- creates a new ServiceRequestItem

Note: Please make sure to leave two blank lines after HTTP GET operations (as seen in the example below)

```XML
POST /sap/c4c/odata/v1/servicerequest/$batch HTTP/1.1
Host: myNNNNNN.crm.ondemand.com
Content-Type: multipart/mixed; boundary=batch_guid_01
Content-Length: 1000
Authorization: Basic <base64encoded(user:pass)>
x-csrf-token: <a_valid_csrf_token>

--batch_guid_01
Content-Type: application/http
Content-Transfer-Encoding:binary

GET ServiceRequestCollection/?$top=3 HTTP/1.1


--batch_guid_01
Content-Type: multipart/mixed; boundary=changeset_guid_01

--changeset_guid_01
Content-Type: application/http
Content-Transfer-Encoding: binary

PATCH ServiceRequestCollection('00163E0DBD9E1ED596EBDFDA564728AC') HTTP/1.1
Content-Type: application/json
Content-ID: 1
Content-Length: 10000

{
	"ServiceRequestUserLifeCycleStatusCode" : "YJ",
	"ScheduledStartDate" : "2015-10-22T00:00:00",
	"ScheduledEndDate" : "2015-10-22T00:00:00",
	"ScheduledStartTime" : "PT13H00M00S",
	"ScheduledEndTime" : "PT15H00M00S"
}

--changeset_guid_01
Content-Type: application/http 
Content-Transfer-Encoding: binary 

POST ServiceRequestItemCollection HTTP/1.1
Content-Type: application/json
Content-ID: 2
Content-Length: 10000

{
    "Description": "1m water hose",
    "ParentObjectID": "00163E0DBD9E1ED596EBDFDA564728AC",
    "ProductID": "10000760"
}

--changeset_guid_01-- 
--batch_guid_01--
```

Here is the response received for the above $batch call:

```XML
--ejjeeffe0
Content-Type: application/http
Content-Length: 18616
Content-Transfer-Encoding: binary

HTTP/1.1 200 OK
Content-Type: application/atom+xml;type=feed
Content-Length: 18503
dataserviceversion: 2.0


<?xml version="1.0" encoding="utf-8"?>
<feed xml:base="https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/servicerequest/" xmlns="http://www.w3.org/2005/Atom" xmlns:m="http://schemas.microsoft.com/ado/2007/08/dataservices/metadata" xmlns:d="http://schemas.microsoft.com/ado/2007/08/dataservices">
    <id>https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/servicerequest/ServiceRequestCollection</id>
    <title type="text">ServiceRequestCollection</title>
    <updated>2015-10-02T23:43:16Z</updated>
    <author>
        <name/>
    </author>
    <link href="ServiceRequestCollection" rel="self" title="ServiceRequestCollection"/>
    <entry>
        <id>https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/servicerequest/ServiceRequestCollection('00163E03A0701ED28A8644CF8D83AE76')</id>
        <title type="text">ServiceRequestCollection('00163E03A0701ED28A8644CF8D83AE76')</title>
        <updated>2015-10-02T23:43:16Z</updated>
        <category term="servicerequest.ServiceRequest" scheme="http://schemas.microsoft.com/ado/2007/08/dataservices/scheme"/>
        <link href="ServiceRequestCollection('00163E03A0701ED28A8644CF8D83AE76')" rel="edit" title="ServiceRequest"/>
        <link href="ServiceRequestCollection('00163E03A0701ED28A8644CF8D83AE76')/AddressSnapshotPostalAddress" rel="http://schemas.microsoft.com/ado/2007/08/dataservices/related/AddressSnapshotPostalAddress" type="application/atom+xml;type=feed" title="AddressSnapshotPostalAddress"/>
        <link href="ServiceRequestCollection('00163E03A0701ED28A8644CF8D83AE76')/ServiceRequestAttachmentFolder" rel="http://schemas.microsoft.com/ado/2007/08/dataservices/related/ServiceRequestAttachmentFolder" type="application/atom+xml;type=feed" title="ServiceRequestAttachmentFolder"/>
        <link href="ServiceRequestCollection('00163E03A0701ED28A8644CF8D83AE76')/ServiceRequestBusinessTransactionDocumentReference" rel="http://schemas.microsoft.com/ado/2007/08/dataservices/related/ServiceRequestBusinessTransactionDocumentReference" type="application/atom+xml;type=feed" title="ServiceRequestBusinessTransactionDocumentReference"/>
        <link href="ServiceRequestCollection('00163E03A0701ED28A8644CF8D83AE76')/ServiceRequestDescription" rel="http://schemas.microsoft.com/ado/2007/08/dataservices/related/ServiceRequestDescription" type="application/atom+xml;type=feed" title="ServiceRequestDescription"/>
        <link href="ServiceRequestCollection('00163E03A0701ED28A8644CF8D83AE76')/ServiceRequestItem" rel="http://schemas.microsoft.com/ado/2007/08/dataservices/related/ServiceRequestItem" type="application/atom+xml;type=feed" title="ServiceRequestItem"/>
        <link href="ServiceRequestCollection('00163E03A0701ED28A8644CF8D83AE76')/ServiceRequestTextCollection" rel="http://schemas.microsoft.com/ado/2007/08/dataservices/related/ServiceRequestTextCollection" type="application/atom+xml;type=feed" title="ServiceRequestTextCollection"/>
        <content type="application/xml">
            <m:properties>
                <d:ActivityCategoryName m:type="servicerequest.MEDIUM_Name">
                    <d:languageCode/>
                    <d:content/>
                </d:ActivityCategoryName>
                <d:ActivityServiceIssueCategoryID/>
                <d:ApprovalStatusCode/>
                <d:ApprovalStatusCodeText/>
                <d:AssignedTechnician/>
                <d:AssignedTo>8000000207</d:AssignedTo>
                <d:CauseCategoryName m:type="servicerequest.MEDIUM_Name">
                    <d:languageCode/>
                    <d:content/>
                </d:CauseCategoryName>
                <d:CauseServiceIssueCategoryID/>
                <d:ChangedBy>Eddie Wright</d:ChangedBy>
                <d:CompletedOnDate m:null="true"/>
                <d:CompletionDueDate>2012-11-08T00:00:00</d:CompletionDueDate>
                <d:Contact/>
                <d:ContractID/>
                <d:CreatedBy>Eddie Wright</d:CreatedBy>
                <d:CreationDate>2012-11-06T00:00:00</d:CreationDate>
                <d:Customer>AgileX</d:Customer>
                <d:CustomerID>1000077</d:CustomerID>
                <d:DataOriginTypeCode>1</d:DataOriginTypeCode>
                <d:DataOriginTypeCodeText>Manual data entry</d:DataOriginTypeCodeText>
                <d:DiagnosticTest>false</d:DiagnosticTest>
                <d:ID>2</d:ID>
                <d:IncidentCategoryName m:type="servicerequest.MEDIUM_Name">
                    <d:languageCode/>
                    <d:content/>
                </d:IncidentCategoryName>
                <d:IncidentServiceIssueCategoryID/>
                <d:InitialResponseDate m:null="true"/>
                <d:InitialReviewDueDate>2012-11-06T00:00:00</d:InitialReviewDueDate>
                <d:InstallationPointID/>
                <d:InstalledBaseID/>
                <d:ItemListServiceRequestExecutionLifeCycleStatusCode>1</d:ItemListServiceRequestExecutionLifeCycleStatusCode>
                <d:ItemListServiceRequestExecutionLifeCycleStatusCodeText>Open</d:ItemListServiceRequestExecutionLifeCycleStatusCodeText>
                <d:LastChangeDate>2012-11-06T00:00:00</d:LastChangeDate>
                <d:LastResponseOnDate m:null="true"/>
                <d:Name>Monarch bike enquiry</d:Name>
                <d:NextResponseDueDate m:null="true"/>
                <d:ObjectCategoryName m:type="servicerequest.MEDIUM_Name">
                    <d:languageCode/>
                    <d:content/>
                </d:ObjectCategoryName>
                <d:ObjectID>00163E03A0701ED28A8644CF8D83AE76</d:ObjectID>
                <d:ObjectServiceIssueCategoryID/>
                <d:Partner/>
                <d:PartnerID/>
                <d:ProcessingTypeCode>SRRQ</d:ProcessingTypeCode>
                <d:ProcessingTypeCodeText>Service Request</d:ProcessingTypeCodeText>
                <d:ProductCategoryDescription/>
                <d:ProductID/>
                <d:ReferenceDate m:null="true"/>
                <d:ReportedForEmail/>
                <d:ReportedForPartyID/>
                <d:ReporterEmail/>
                <d:ReporterPartyID/>
                <d:RequestAssignmentStatusCode>1</d:RequestAssignmentStatusCode>
                <d:RequestAssignmentStatusCodeText>Processor Action</d:RequestAssignmentStatusCodeText>
                <d:RequestedEnd/>
                <d:RequestedEndTimeZoneCode/>
                <d:RequestedStart/>
                <d:RequestedStartTimeZoneCode/>
                <d:SalesTerritoryID/>
                <d:ScheduledEndDate m:null="true"/>
                <d:ScheduledEndTime>PT00H00M00S</d:ScheduledEndTime>
                <d:ScheduledStartDate m:null="true"/>
                <d:ScheduledStartTime>PT00H00M00S</d:ScheduledStartTime>
                <d:SerialID/>
                <d:ServiceAndSupportTeam>S3110</d:ServiceAndSupportTeam>
                <d:ServiceCategoryName m:type="servicerequest.MEDIUM_Name">
                    <d:languageCode/>
                    <d:content/>
                </d:ServiceCategoryName>
                <d:ServiceIssueCategoryID/>
                <d:ServiceLevelAgreement/>
                <d:ServicePriorityCode>3</d:ServicePriorityCode>
                <d:ServicePriorityCodeText>Normal</d:ServicePriorityCodeText>
                <d:ServiceRequestClassificationCode/>
                <d:ServiceRequestClassificationCodeText/>
                <d:ServiceRequestLifeCycleStatusCode>1</d:ServiceRequestLifeCycleStatusCode>
                <d:ServiceRequestLifeCycleStatusCodeText>Open</d:ServiceRequestLifeCycleStatusCodeText>
                <d:ServiceRequestUserLifeCycleStatusCode>1</d:ServiceRequestUserLifeCycleStatusCode>
                <d:ServiceRequestUserLifeCycleStatusCodeText>Open</d:ServiceRequestUserLifeCycleStatusCodeText>
                <d:ServiceTechnician/>
                <d:ServiceTechnicianTeam/>
                <d:Technician/>
                <d:WarrantyDescription/>
                <d:WarrantyFrom m:null="true"/>
                <d:WarrantyTo m:null="true"/>
            </m:properties>
        </content>
    </entry>
    <entry>
        <id>https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/servicerequest/ServiceRequestCollection('00163E03A0701ED28DABB39DFBC5A0FD')</id>
        <title type="text">ServiceRequestCollection('00163E03A0701ED28DABB39DFBC5A0FD')</title>
        <updated>2015-10-02T23:43:16Z</updated>
        <category term="servicerequest.ServiceRequest" scheme="http://schemas.microsoft.com/ado/2007/08/dataservices/scheme"/>
        <link href="ServiceRequestCollection('00163E03A0701ED28DABB39DFBC5A0FD')" rel="edit" title="ServiceRequest"/>
        <link href="ServiceRequestCollection('00163E03A0701ED28DABB39DFBC5A0FD')/AddressSnapshotPostalAddress" rel="http://schemas.microsoft.com/ado/2007/08/dataservices/related/AddressSnapshotPostalAddress" type="application/atom+xml;type=feed" title="AddressSnapshotPostalAddress"/>
        <link href="ServiceRequestCollection('00163E03A0701ED28DABB39DFBC5A0FD')/ServiceRequestAttachmentFolder" rel="http://schemas.microsoft.com/ado/2007/08/dataservices/related/ServiceRequestAttachmentFolder" type="application/atom+xml;type=feed" title="ServiceRequestAttachmentFolder"/>
        <link href="ServiceRequestCollection('00163E03A0701ED28DABB39DFBC5A0FD')/ServiceRequestBusinessTransactionDocumentReference" rel="http://schemas.microsoft.com/ado/2007/08/dataservices/related/ServiceRequestBusinessTransactionDocumentReference" type="application/atom+xml;type=feed" title="ServiceRequestBusinessTransactionDocumentReference"/>
        <link href="ServiceRequestCollection('00163E03A0701ED28DABB39DFBC5A0FD')/ServiceRequestDescription" rel="http://schemas.microsoft.com/ado/2007/08/dataservices/related/ServiceRequestDescription" type="application/atom+xml;type=feed" title="ServiceRequestDescription"/>
        <link href="ServiceRequestCollection('00163E03A0701ED28DABB39DFBC5A0FD')/ServiceRequestItem" rel="http://schemas.microsoft.com/ado/2007/08/dataservices/related/ServiceRequestItem" type="application/atom+xml;type=feed" title="ServiceRequestItem"/>
        <link href="ServiceRequestCollection('00163E03A0701ED28DABB39DFBC5A0FD')/ServiceRequestTextCollection" rel="http://schemas.microsoft.com/ado/2007/08/dataservices/related/ServiceRequestTextCollection" type="application/atom+xml;type=feed" title="ServiceRequestTextCollection"/>
        <content type="application/xml">
            <m:properties>
                <d:ActivityCategoryName m:type="servicerequest.MEDIUM_Name">
                    <d:languageCode/>
                    <d:content/>
                </d:ActivityCategoryName>
                <d:ActivityServiceIssueCategoryID/>
                <d:ApprovalStatusCode/>
                <d:ApprovalStatusCodeText/>
                <d:AssignedTechnician/>
                <d:AssignedTo>8000000207</d:AssignedTo>
                <d:CauseCategoryName m:type="servicerequest.MEDIUM_Name">
                    <d:languageCode/>
                    <d:content/>
                </d:CauseCategoryName>
                <d:CauseServiceIssueCategoryID/>
                <d:ChangedBy>Eddie Wright</d:ChangedBy>
                <d:CompletedOnDate>2012-07-20T00:00:00</d:CompletedOnDate>
                <d:CompletionDueDate>2012-07-25T00:00:00</d:CompletionDueDate>
                <d:Contact>Catherine Lesjak</d:Contact>
                <d:ContractID/>
                <d:CreatedBy>Eddie Wright</d:CreatedBy>
                <d:CreationDate>2012-11-23T00:00:00</d:CreationDate>
                <d:Customer>HP</d:Customer>
                <d:CustomerID>10332</d:CustomerID>
                <d:DataOriginTypeCode>1</d:DataOriginTypeCode>
                <d:DataOriginTypeCodeText>Manual data entry</d:DataOriginTypeCodeText>
                <d:DiagnosticTest>false</d:DiagnosticTest>
                <d:ID>1423</d:ID>
                <d:IncidentCategoryName m:type="servicerequest.MEDIUM_Name">
                    <d:languageCode/>
                    <d:content/>
                </d:IncidentCategoryName>
                <d:IncidentServiceIssueCategoryID/>
                <d:InitialResponseDate>2012-07-20T00:00:00</d:InitialResponseDate>
                <d:InitialReviewDueDate>2012-07-20T00:00:00</d:InitialReviewDueDate>
                <d:InstallationPointID/>
                <d:InstalledBaseID/>
                <d:ItemListServiceRequestExecutionLifeCycleStatusCode>1</d:ItemListServiceRequestExecutionLifeCycleStatusCode>
                <d:ItemListServiceRequestExecutionLifeCycleStatusCodeText>Open</d:ItemListServiceRequestExecutionLifeCycleStatusCodeText>
                <d:LastChangeDate>2014-08-15T00:00:00</d:LastChangeDate>
                <d:LastResponseOnDate m:null="true"/>
                <d:Name>System issue at HP</d:Name>
                <d:NextResponseDueDate m:null="true"/>
                <d:ObjectCategoryName m:type="servicerequest.MEDIUM_Name">
                    <d:languageCode/>
                    <d:content/>
                </d:ObjectCategoryName>
                <d:ObjectID>00163E03A0701ED28DABB39DFBC5A0FD</d:ObjectID>
                <d:ObjectServiceIssueCategoryID/>
                <d:Partner/>
                <d:PartnerID/>
                <d:ProcessingTypeCode>SRRQ</d:ProcessingTypeCode>
                <d:ProcessingTypeCodeText>Service Request</d:ProcessingTypeCodeText>
                <d:ProductCategoryDescription>B2C - Travel</d:ProductCategoryDescription>
                <d:ProductID>P110100</d:ProductID>
                <d:ReferenceDate m:null="true"/>
                <d:ReportedForEmail/>
                <d:ReportedForPartyID/>
                <d:ReporterEmail>c.lesjak@HP.COM</d:ReporterEmail>
                <d:ReporterPartyID>1000394</d:ReporterPartyID>
                <d:RequestAssignmentStatusCode>2</d:RequestAssignmentStatusCode>
                <d:RequestAssignmentStatusCodeText>Requestor Action</d:RequestAssignmentStatusCodeText>
                <d:RequestedEnd/>
                <d:RequestedEndTimeZoneCode/>
                <d:RequestedStart/>
                <d:RequestedStartTimeZoneCode/>
                <d:SalesTerritoryID/>
                <d:ScheduledEndDate m:null="true"/>
                <d:ScheduledEndTime>PT00H00M00S</d:ScheduledEndTime>
                <d:ScheduledStartDate m:null="true"/>
                <d:ScheduledStartTime>PT00H00M00S</d:ScheduledStartTime>
                <d:SerialID/>
                <d:ServiceAndSupportTeam>S3110</d:ServiceAndSupportTeam>
                <d:ServiceCategoryName m:type="servicerequest.MEDIUM_Name">
                    <d:languageCode>E</d:languageCode>
                    <d:content>Product Support</d:content>
                </d:ServiceCategoryName>
                <d:ServiceIssueCategoryID>PS</d:ServiceIssueCategoryID>
                <d:ServiceLevelAgreement>CLOUDFORSERVICE_STANDARD</d:ServiceLevelAgreement>
                <d:ServicePriorityCode>3</d:ServicePriorityCode>
                <d:ServicePriorityCodeText>Normal</d:ServicePriorityCodeText>
                <d:ServiceRequestClassificationCode/>
                <d:ServiceRequestClassificationCodeText/>
                <d:ServiceRequestLifeCycleStatusCode>3</d:ServiceRequestLifeCycleStatusCode>
                <d:ServiceRequestLifeCycleStatusCodeText>Completed</d:ServiceRequestLifeCycleStatusCodeText>
                <d:ServiceRequestUserLifeCycleStatusCode>5</d:ServiceRequestUserLifeCycleStatusCode>
                <d:ServiceRequestUserLifeCycleStatusCodeText>Completed</d:ServiceRequestUserLifeCycleStatusCodeText>
                <d:ServiceTechnician>John Smith</d:ServiceTechnician>
                <d:ServiceTechnicianTeam/>
                <d:Technician/>
                <d:WarrantyDescription/>
                <d:WarrantyFrom m:null="true"/>
                <d:WarrantyTo m:null="true"/>
            </m:properties>
        </content>
    </entry>
    <entry>
        <id>https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/servicerequest/ServiceRequestCollection('00163E03A0701ED28DABB39DFBC660FD')</id>
        <title type="text">ServiceRequestCollection('00163E03A0701ED28DABB39DFBC660FD')</title>
        <updated>2015-10-02T23:43:16Z</updated>
        <category term="servicerequest.ServiceRequest" scheme="http://schemas.microsoft.com/ado/2007/08/dataservices/scheme"/>
        <link href="ServiceRequestCollection('00163E03A0701ED28DABB39DFBC660FD')" rel="edit" title="ServiceRequest"/>
        <link href="ServiceRequestCollection('00163E03A0701ED28DABB39DFBC660FD')/AddressSnapshotPostalAddress" rel="http://schemas.microsoft.com/ado/2007/08/dataservices/related/AddressSnapshotPostalAddress" type="application/atom+xml;type=feed" title="AddressSnapshotPostalAddress"/>
        <link href="ServiceRequestCollection('00163E03A0701ED28DABB39DFBC660FD')/ServiceRequestAttachmentFolder" rel="http://schemas.microsoft.com/ado/2007/08/dataservices/related/ServiceRequestAttachmentFolder" type="application/atom+xml;type=feed" title="ServiceRequestAttachmentFolder"/>
        <link href="ServiceRequestCollection('00163E03A0701ED28DABB39DFBC660FD')/ServiceRequestBusinessTransactionDocumentReference" rel="http://schemas.microsoft.com/ado/2007/08/dataservices/related/ServiceRequestBusinessTransactionDocumentReference" type="application/atom+xml;type=feed" title="ServiceRequestBusinessTransactionDocumentReference"/>
        <link href="ServiceRequestCollection('00163E03A0701ED28DABB39DFBC660FD')/ServiceRequestDescription" rel="http://schemas.microsoft.com/ado/2007/08/dataservices/related/ServiceRequestDescription" type="application/atom+xml;type=feed" title="ServiceRequestDescription"/>
        <link href="ServiceRequestCollection('00163E03A0701ED28DABB39DFBC660FD')/ServiceRequestItem" rel="http://schemas.microsoft.com/ado/2007/08/dataservices/related/ServiceRequestItem" type="application/atom+xml;type=feed" title="ServiceRequestItem"/>
        <link href="ServiceRequestCollection('00163E03A0701ED28DABB39DFBC660FD')/ServiceRequestTextCollection" rel="http://schemas.microsoft.com/ado/2007/08/dataservices/related/ServiceRequestTextCollection" type="application/atom+xml;type=feed" title="ServiceRequestTextCollection"/>
        <content type="application/xml">
            <m:properties>
                <d:ActivityCategoryName m:type="servicerequest.MEDIUM_Name">
                    <d:languageCode/>
                    <d:content/>
                </d:ActivityCategoryName>
                <d:ActivityServiceIssueCategoryID/>
                <d:ApprovalStatusCode/>
                <d:ApprovalStatusCodeText/>
                <d:AssignedTechnician/>
                <d:AssignedTo>8000000197</d:AssignedTo>
                <d:CauseCategoryName m:type="servicerequest.MEDIUM_Name">
                    <d:languageCode/>
                    <d:content/>
                </d:CauseCategoryName>
                <d:CauseServiceIssueCategoryID/>
                <d:ChangedBy>Juliane Beyer</d:ChangedBy>
                <d:CompletedOnDate>2012-06-09T00:00:00</d:CompletedOnDate>
                <d:CompletionDueDate>2012-06-13T00:00:00</d:CompletionDueDate>
                <d:Contact>Frank Sander</d:Contact>
                <d:ContractID/>
                <d:CreatedBy>Eddie Wright</d:CreatedBy>
                <d:CreationDate>2012-11-23T00:00:00</d:CreationDate>
                <d:Customer>Digital Plus AG</d:Customer>
                <d:CustomerID>10025</d:CustomerID>
                <d:DataOriginTypeCode>1</d:DataOriginTypeCode>
                <d:DataOriginTypeCodeText>Manual data entry</d:DataOriginTypeCodeText>
                <d:DiagnosticTest>false</d:DiagnosticTest>
                <d:ID>1422</d:ID>
                <d:IncidentCategoryName m:type="servicerequest.MEDIUM_Name">
                    <d:languageCode/>
                    <d:content/>
                </d:IncidentCategoryName>
                <d:IncidentServiceIssueCategoryID/>
                <d:InitialResponseDate>2012-06-09T00:00:00</d:InitialResponseDate>
                <d:InitialReviewDueDate>2012-06-11T00:00:00</d:InitialReviewDueDate>
                <d:InstallationPointID/>
                <d:InstalledBaseID/>
                <d:ItemListServiceRequestExecutionLifeCycleStatusCode>1</d:ItemListServiceRequestExecutionLifeCycleStatusCode>
                <d:ItemListServiceRequestExecutionLifeCycleStatusCodeText>Open</d:ItemListServiceRequestExecutionLifeCycleStatusCodeText>
                <d:LastChangeDate>2014-05-20T00:00:00</d:LastChangeDate>
                <d:LastResponseOnDate m:null="true"/>
                <d:Name>System issue at Digital Plus AG</d:Name>
                <d:NextResponseDueDate>2012-08-05T00:00:00</d:NextResponseDueDate>
                <d:ObjectCategoryName m:type="servicerequest.MEDIUM_Name">
                    <d:languageCode/>
                    <d:content/>
                </d:ObjectCategoryName>
                <d:ObjectID>00163E03A0701ED28DABB39DFBC660FD</d:ObjectID>
                <d:ObjectServiceIssueCategoryID/>
                <d:Partner/>
                <d:PartnerID/>
                <d:ProcessingTypeCode>SRRQ</d:ProcessingTypeCode>
                <d:ProcessingTypeCodeText>Service Request</d:ProcessingTypeCodeText>
                <d:ProductCategoryDescription>B2C - HT - Imaging</d:ProductCategoryDescription>
                <d:ProductID>P600103</d:ProductID>
                <d:ReferenceDate m:null="true"/>
                <d:ReportedForEmail/>
                <d:ReportedForPartyID/>
                <d:ReporterEmail>Frank.Sander@ONDEMAND.COM</d:ReporterEmail>
                <d:ReporterPartyID>1000118</d:ReporterPartyID>
                <d:RequestAssignmentStatusCode>2</d:RequestAssignmentStatusCode>
                <d:RequestAssignmentStatusCodeText>Requestor Action</d:RequestAssignmentStatusCodeText>
                <d:RequestedEnd/>
                <d:RequestedEndTimeZoneCode/>
                <d:RequestedStart/>
                <d:RequestedStartTimeZoneCode/>
                <d:SalesTerritoryID/>
                <d:ScheduledEndDate m:null="true"/>
                <d:ScheduledEndTime>PT00H00M00S</d:ScheduledEndTime>
                <d:ScheduledStartDate m:null="true"/>
                <d:ScheduledStartTime>PT00H00M00S</d:ScheduledStartTime>
                <d:SerialID/>
                <d:ServiceAndSupportTeam>S3111</d:ServiceAndSupportTeam>
                <d:ServiceCategoryName m:type="servicerequest.MEDIUM_Name">
                    <d:languageCode>E</d:languageCode>
                    <d:content>General Usage Issue</d:content>
                </d:ServiceCategoryName>
                <d:ServiceIssueCategoryID>PS-GEN</d:ServiceIssueCategoryID>
                <d:ServiceLevelAgreement>CLOUDFORSERVICE_SLA_VIP</d:ServiceLevelAgreement>
                <d:ServicePriorityCode>2</d:ServicePriorityCode>
                <d:ServicePriorityCodeText>Urgent</d:ServicePriorityCodeText>
                <d:ServiceRequestClassificationCode/>
                <d:ServiceRequestClassificationCodeText/>
                <d:ServiceRequestLifeCycleStatusCode>2</d:ServiceRequestLifeCycleStatusCode>
                <d:ServiceRequestLifeCycleStatusCodeText>In Process</d:ServiceRequestLifeCycleStatusCodeText>
                <d:ServiceRequestUserLifeCycleStatusCode>4</d:ServiceRequestUserLifeCycleStatusCode>
                <d:ServiceRequestUserLifeCycleStatusCodeText>In Process - Waiting for Customer</d:ServiceRequestUserLifeCycleStatusCodeText>
                <d:ServiceTechnician/>
                <d:ServiceTechnicianTeam/>
                <d:Technician/>
                <d:WarrantyDescription/>
                <d:WarrantyFrom m:null="true"/>
                <d:WarrantyTo m:null="true"/>
            </m:properties>
        </content>
    </entry>
</feed>
--ejjeeffe0
Content-Type: multipart/mixed; boundary=ejjeeffe1
Content-Length:      2329

--ejjeeffe1
Content-Type: application/http
Content-Length: 96
Content-Transfer-Encoding: binary

HTTP/1.1 204 No Content
Content-Type: text/html
Content-Length: 0
dataserviceversion: 2.0


--ejjeeffe1
Content-Type: application/http
Content-Length: 2008
Content-Transfer-Encoding: binary

HTTP/1.1 201 Created
Content-Type: application/atom+xml;type=entry
Content-Length: 1716
location: https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/servicerequest/ServiceRequestItemCollection('00163E0DBD9E1ED59AABEB57FF4A3F4B')
dataserviceversion: 2.0
cache-control: no-cache, no-store


<?xml version="1.0" encoding="utf-8"?>
<entry xml:base="https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/servicerequest/" xmlns="http://www.w3.org/2005/Atom" xmlns:m="http://schemas.microsoft.com/ado/2007/08/dataservices/metadata" xmlns:d="http://schemas.microsoft.com/ado/2007/08/dataservices">
    <id>https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/servicerequest/ServiceRequestItemCollection('00163E0DBD9E1ED59AABEB57FF4A3F4B')</id>
    <title type="text">ServiceRequestItemCollection('00163E0DBD9E1ED59AABEB57FF4A3F4B')</title>
    <updated>2015-10-02T23:43:20Z</updated>
    <category term="servicerequest.ServiceRequestItem" scheme="http://schemas.microsoft.com/ado/2007/08/dataservices/scheme"/>
    <link href="ServiceRequestItemCollection('00163E0DBD9E1ED59AABEB57FF4A3F4B')" rel="edit" title="ServiceRequestItem"/>
    <link href="ServiceRequestItemCollection('00163E0DBD9E1ED59AABEB57FF4A3F4B')/ServiceRequest" rel="http://schemas.microsoft.com/ado/2007/08/dataservices/related/ServiceRequest" type="application/atom+xml;type=entry" title="ServiceRequest"/>
    <link href="ServiceRequestItemCollection('00163E0DBD9E1ED59AABEB57FF4A3F4B')/ServiceRequestFirstRequestedItemScheduleLine" rel="http://schemas.microsoft.com/ado/2007/08/dataservices/related/ServiceRequestFirstRequestedItemScheduleLine" type="application/atom+xml;type=entry" title="ServiceRequestFirstRequestedItemScheduleLine"/>
    <content type="application/xml">
        <m:properties>
            <d:Description>1m water hose</d:Description>
            <d:ObjectID>00163E0DBD9E1ED59AABEB57FF4A3F4B</d:ObjectID>
            <d:ParentObjectID>00163E0DBD9E1ED596EBDFDA564728AC</d:ParentObjectID>
            <d:ProductCategoryDescription>Parts</d:ProductCategoryDescription>
            <d:ProductID>10000760</d:ProductID>
        </m:properties>
    </content>
</entry>
--ejjeeffe1--

--ejjeeffe0--
```


#### $expand

C4C OData API's support for $expand system query option is via Navigaton Properties. 

E.g. `/AccountCollection?$top=10&$format=json&$expand=AccountMainAddress` Where AccountMainAddress is a Navigation Property defined in the Entity Data Model (EDM) for the Account Entity (see the Entity defintion below).

```XML
			<EntityType Name="Account">
				<Key>
					<PropertyRef Name="ObjectID"/>
				</Key>
				<Property Name="ABCClassificationCode" Type="Edm.String" Nullable="true" MaxLength="1" FixedLength="true" sap:creatable="true" sap:updatable="true" sap:filterable="false"/>
				<Property Name="ABCClassificationCodeText" Type="Edm.String" Nullable="true" sap:creatable="false" sap:updatable="false" sap:filterable="false"/>
				<Property Name="AccountFormattedName" Type="Edm.String" Nullable="false" MaxLength="40" FixedLength="true" sap:creatable="false" sap:updatable="false" sap:filterable="false"/>
				<Property Name="AccountID" Type="Edm.String" Nullable="true" MaxLength="10" FixedLength="true" sap:creatable="true" sap:updatable="true" sap:filterable="false"/>
				<Property Name="AccountName" Type="Edm.String" Nullable="true" MaxLength="240" FixedLength="true" sap:creatable="false" sap:updatable="true" sap:filterable="false"/>
....
				<NavigationProperty Name="AccountMainAddress" Relationship="http://sap.com/xi/AP/CRM/Global.Account_AccountMainAddress" FromRole="Account" ToRole="AccountMainAddress"/>
				<NavigationProperty Name="AccountNotes" Relationship="http://sap.com/xi/AP/CRM/Global.Account_AccountNotes" FromRole="Account" ToRole="AccountNotes"/>
				<NavigationProperty Name="AccountRole" Relationship="http://sap.com/xi/AP/CRM/Global.Account_AccountRole" FromRole="Account" ToRole="AccountRole"/>
				<NavigationProperty Name="AccountSalesData" Relationship="http://sap.com/xi/AP/CRM/Global.Account_AccountSalesData" FromRole="Account" ToRole="AccountSalesData"/>
				<NavigationProperty Name="AccountTeam" Relationship="http://sap.com/xi/AP/CRM/Global.Account_AccountTeam" FromRole="Account" ToRole="AccountTeam"/>
				<NavigationProperty Name="ExternalIDMapping" Relationship="http://sap.com/xi/AP/CRM/Global.Account_ExternalIDMapping" FromRole="Account" ToRole="ExternalIDMapping"/>
			</EntityType>
```			

For every entity (that is not a Header entity e.g. Account or Opportunity etc.) the C4C OData framework implicitly generates a Navigation Property to the parent entity as well as a Navigation Property to the Header entity (if the parent entity is not a Header itself). 

If you consider the metadata below, the following can be seen:

  * Opportunity is the Header entity
  * OpportunityItem has the Opportunity as the Parent (as well as the Header) and 
  * OpportunityItemRevenuePlanReporting has OpportunityItem as the Parent and Opportunity as the Header entity

For each of the entities below, a Navigation Property is available that allows a given Entity type to navigate to the immediate parent e.g. OpportunityItemRevenuePlanReporting has a 

  * Navigation Property Opportunity that allows navigation to the Header and 
  * Navigation Property OpportunityItem that allows navigation to the Parent.

This pattern holds true for all OData services generated by C4C (both standard as well as custom services).

```XML
			<EntityType Name="Opportunity">
				<Key>
					<PropertyRef Name="ObjectID"/>
				</Key>
				<Property Name="ApprovalStatusCode" Type="Edm.String" Nullable="true" MaxLength="2" FixedLength="true" sap:creatable="false" sap:updatable="false" sap:filterable="false"/>
				<Property Name="ConsistencyStatusCode" Type="Edm.String" Nullable="true" MaxLength="2" FixedLength="true" sap:creatable="false" sap:updatable="false" sap:filterable="false"/>
				<Property Name="ID" Type="Edm.String" Nullable="true" MaxLength="35" FixedLength="true" sap:creatable="true" sap:updatable="true" sap:filterable="false"/>
				<Property Name="LifeCycleStatusCode" Type="Edm.String" Nullable="true" MaxLength="2" FixedLength="true" sap:creatable="false" sap:updatable="false" sap:filterable="false"/>
				<Property Name="Name" Type="Edm.String" Nullable="true" MaxLength="765" FixedLength="true" sap:creatable="true" sap:updatable="true" sap:filterable="false"/>
				<Property Name="ObjectID" Type="Edm.String" Nullable="true" MaxLength="70" FixedLength="true" sap:creatable="false" sap:updatable="false" sap:filterable="false"/>
				<Property Name="OriginTypeCode" Type="Edm.String" Nullable="true" MaxLength="3" FixedLength="true" sap:creatable="true" sap:updatable="true" sap:filterable="false"/>
				<Property Name="PhaseProgressEvaluationStatusCode" Type="Edm.String" Nullable="true" MaxLength="2" FixedLength="true" sap:creatable="false" sap:updatable="false" sap:filterable="false"/>
				<Property Name="PriorityCode" Type="Edm.String" Nullable="true" MaxLength="1" FixedLength="true" sap:creatable="true" sap:updatable="true" sap:filterable="false"/>
				<Property Name="ProcessingTypeCode" Type="Edm.String" Nullable="true" MaxLength="4" FixedLength="true" sap:creatable="true" sap:updatable="true" sap:filterable="false"/>
				<NavigationProperty Name="OpportunityItem" Relationship="cust.Opportunity_OpportunityItem" FromRole="Opportunity" ToRole="OpportunityItem"/>
			</EntityType>
			<EntityType Name="OpportunityItem">
				<Key>
					<PropertyRef Name="ObjectID"/>
				</Key>
				<Property Name="ID" Type="Edm.String" Nullable="true" MaxLength="10" FixedLength="true" sap:creatable="true" sap:updatable="true" sap:filterable="false"/>
				<Property Name="NetAmount" Type="cust.Amount" Nullable="true" sap:creatable="false" sap:updatable="false" sap:filterable="false"/>
				<Property Name="ObjectID" Type="Edm.String" Nullable="true" MaxLength="70" FixedLength="true" sap:creatable="false" sap:updatable="false" sap:filterable="false"/>
				<Property Name="ParentObjectID" Type="Edm.String" Nullable="true" MaxLength="70" FixedLength="true" sap:creatable="false" sap:updatable="false" sap:filterable="false"/>
				<Property Name="ProductID" Type="Edm.String" Nullable="true" MaxLength="60" FixedLength="true" sap:creatable="true" sap:updatable="true" sap:filterable="false"/>
				<Property Name="Quantity" Type="cust.Quantity" Nullable="true" sap:creatable="true" sap:updatable="true" sap:filterable="false"/>
				<NavigationProperty Name="Opportunity" Relationship="cust.Opportunity_OpportunityItem" FromRole="OpportunityItem" ToRole="Opportunity"/>
				<NavigationProperty Name="OpportunityItemRevenuePlanReporting" Relationship="cust.OpportunityItem_OpportunityItemRevenuePlanReporting" FromRole="OpportunityItem" ToRole="OpportunityItemRevenuePlanReporting"/>
			</EntityType>
			<EntityType Name="OpportunityItemRevenuePlanReporting">
				<Key>
					<PropertyRef Name="ObjectID"/>
				</Key>
				<Property Name="DistributionAmount" Type="cust.Amount" Nullable="true" sap:creatable="true" sap:updatable="true" sap:filterable="false"/>
				<Property Name="DistributionDate" Type="Edm.DateTime" Nullable="true" Precision="8" sap:creatable="true" sap:updatable="true" sap:filterable="false"/>
				<Property Name="ObjectID" Type="Edm.String" Nullable="true" MaxLength="70" FixedLength="true" sap:creatable="false" sap:updatable="false" sap:filterable="false"/>
				<Property Name="ParentObjectID" Type="Edm.String" Nullable="true" MaxLength="70" FixedLength="true" sap:creatable="false" sap:updatable="false" sap:filterable="false"/>
				<Property Name="ReportingCurrencyDistributionAmount" Type="cust.Amount" Nullable="true" sap:creatable="true" sap:updatable="true" sap:filterable="false"/>
				<Property Name="RevenuePartnerUUID" Type="Edm.Guid" Nullable="true" sap:creatable="true" sap:updatable="true" sap:filterable="false"/>
				<NavigationProperty Name="Opportunity" Relationship="cust.Opportunity_OpportunityItemRevenuePlanReporting" FromRole="OpportunityItemRevenuePlanReporting" ToRole="Opportunity"/>
				<NavigationProperty Name="OpportunityItem" Relationship="cust.OpportunityItem_OpportunityItemRevenuePlanReporting" FromRole="OpportunityItemRevenuePlanReporting" ToRole="OpportunityItem"/>
			</EntityType>
```			
Please note that C4C OData API currently **DOES NOT** support the usage of properties from expanded navigations as part of $filter conditions. However, this can be achieved by applying the $filter condition at the child entity collection and then use an $expand condition to navigate to either the Parent collection or to the Header collection. 
E.g. if the requirement is to get all Opportunities that have a certain Product, as a part of a single GET request, this could be achieved with the following query call:

```
/OpportunityItemCollection?$format=json&$filter=ProductID eq 'P300104'&$expand=Opportunity
```

**Performance considerations:** Due to their complex nature, $expand queries require additional processing in comparison to the queries involving a single entity. Thus, in general $expand queries can be slow and in some cases considerably slow. While this should not impact the performance requirements of a result set with few records (e.g. single READ operation), when the result set contains a number of records, performance might not be ideal. Therefore, we recommend that applications should be designed accordingly and if $expand query doesn't meet the performance requirements, implement alternative methods. Due to the reasons above, queries involving $expand will only return the first 100 records and if there are more records matching the criteria, at the end of the result, a URL link to get next 100 records will be included. If a larger result set is desired, appropriate paging can be implemented via $skip and $top.


#### $filter

Option | Example | Description
-------|---------|------------
eq | /OpportunityCollection?$filter=AccountID eq '1001910' <br><br> /UserCollection?$filter=UserID eq '\*ADMIN\*'| Gets all Opportunity entries that matches the specified AccountID <br><br> Matches UserID containing the string ADMIN - '*' can be used as a wildcard.
ge, le |  /OpportunityCollection?$filter=AccountID ge '1001910' and AccountID le '1001920' | Gets all Opportunity entries with AccountID within the specified range
datetimeoffset| /AccountCollection?$filter=CreatedOn ge datetimeoffset'2015-04-01T00:00:00Z' | Accounts created on or after given datetime
endswith | /AccountCollection?$filter=endswith(AccountName,'LLC') | All accounts whose AccountName ends with 'LLC'. **_Note that the Property Name has to be specified first_**.
startswith | /AccountCollection?$filter=startswith(AccountName,'Porter') | All accounts whose AccountName starts with 'Porter'. **_Similar to endswith note that the Property Name has to be specified first_**.


#### $inlinecount

XML response with inlinecount. The Element <m:count> contains the response to the $inlinecount.

```XML
<feed xmlns="http://www.w3.org/2005/Atom" xmlns:m="http://schemas.microsoft.com/ado/2007/08/dataservices/metadata" xmlns:d="http://schemas.microsoft.com/ado/2007/08/dataservices" xml:base="https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/c4codataapi/">
	<id>https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/v1/c4codataapi/OpportunityCollection</id>
	<title type="text">OpportunityCollection</title>
	<updated>2015-08-23T17:30:32Z</updated>
	<author>
		<name/>
	</author>
	<link href="OpportunityCollection" rel="self" title="OpportunityCollection"/>
	<m:count>39080</m:count>
	<entry>
		<id>https://myNNNNN.crm.ondemand.com/sap/c4c/odata/v1/c4codataapi/OpportunityCollection('00163E03A0701ED28BCEC7F4AA474109')</id>
		<title type="text">OpportunityCollection('00163E03A0701ED28BCEC7F4AA474109')</title>
		<updated>2015-08-23T17:30:32Z</updated>
		....
```

JSON response with inlinecount. The attribute __count contains the response to the $inlinecount.

```JSON
{
  "d": {
    "__count": "39080", 
    "results": [
      {
        "AccountID": "10009", 
        "AccountName": {
          "__metadata": {
            "type": "http://sap.com/xi/AP/CRM/Global.ENCRYPTED_LONG_Name"
          }, 
          "content": "Primo Sustainable products", 
          "languageCode": "E"
        }, 
        ...
```

#### $search
Although it is not part of OData V2 specification, $search is supported by C4C OData API. Once $search is performed, C4C OData API compares the term provided to $search against the properties marked as $search relevant in OData Service Expolorer. Standard C4C OData services are delievered with $search relevant properties marked. 

For custom OData services, it is possible to mark $search releveant entity properties individually as well as at the entity collection level. 

Please note that the term passed to $search should not be bound by any quotes or double-quotes even when the term contains spaces. The following example shows the usage of the $search.:

```
https://myNNNNNN.crm.ondemand.com/sap/byd/odata/cust/v1/c4codataapi/CustomerCollection?$search=test user
```

### ETag Support
[HTTP ETag](https://en.wikipedia.org/wiki/HTTP_ETag) (entity tag) is mainly used for [optimistic concurrency control](https://en.wikipedia.org/wiki/Optimistic_concurrency_control) and client side caching of data. Since C4C 1602, OData API provides support for weak validation of ETags.

```
W/"datetimeoffset'2016-04-27T23%3A07%3A31.6809330Z'"
```

Each C4C entity has an associated ETag (which indicates the last updated datetime of that entity). Thus, when a collection of C4C entities are read, associated ETag of each entity is returned in its ETag ETag property. 


```
{
  "d": {
    "results": [
      {
        "__metadata": {
          "uri": "https://my315537.crm.ondemand.com/sap/c4c/odata/v1/c4codataapi/
          ProductCollection('00163E03A0701EE288BE9895233EBD27')",
          "type": "c4codataapi.Product",
          "etag": "W/\"datetimeoffset'2015-02-03T21%3A07%3A03.5328420Z'\""
        },
        "ObjectID": "00163E03A0701EE288BE9895233EBD27",
        "ID": "P140100",
        "UUID": "00163E03-A070-1EE2-88BE-9895233EBD27",
        "CreatedOn": "/Date(1351534600660)/",
        "CreatedBy": "SAP WORKER",
        "ChangedOn": "/Date(1422997623532)/",
        "StatusCode": "3",
        "UnitOfMeasureCode": "EA",
        "Description": "GS Marengo Womens Mountain Bike",
        "languageCode": "E",
        "ETag": "/Date(1422997623532)/",	<============== ETag in the entity record
        "StatusCodeText": "Blocked",
        "UnitOfMeasureCodeText": "Each",
        "languageCodeText": "English",
 ...
```

In the case where a single C4C Entity is read, in addition to the ETag property, the ETag is also returned as part of the response header.

```
c4c-odata-response-time →2061 ms
cache-control →no-cache, no-store
content-encoding →gzip
content-length →505
content-type →application/json; charset=utf-8
dataserviceversion →2.0
etag →W/"datetimeoffset'2015-02-03T21%3A07%3A03.5328420Z'"   <<========== ETag in response header
x-csrf-token →dQOr2DmqinUDkzVKub0L4A==
```

#### Optimistic Concurrency Control with ETag
When ETag is used for optimistic concurrency control following scenario is implemented:

* Client application receives the ETag associated with the entity read
* While modifiying the record in the server via HTTP PUT, PATCH or DELETE, the client sets the HTTP request header <i>If-Match</i> with the ETag previously read.
* If ETag associated with the entity in the server matches the ETag passed in the request header, the call succeeds with <i>HTTP response 204</i>. (I.e. no other updates has been made since the client read the entity record)
* Otherwise, the HTTP response gets the status <i>412 Precondition Failed</i> (i.e. The conurrency check fails)

Example HTTP PUT request with concurrency control:

```
	PUT AccountCollection HTTP/1.1
	host: <your tenant>
	content-type: application/json
	if-match: W/"datetimeoffset'2015-02-03T21%3A07%3A03.5328420Z'"
	x-csrf-token: dQOr2DmqinUDkzVKub0L4A==

	{
		"AccountName":"New Name of the account"
	}

```

## Sample Payloads

  * [Service Ticket](sections/serviceticket.md)
  * [Mass query pattern](sections/massquery.md)
  * [Sample C4C OData API V2 calls](sections/odataapi_v2_samples.md)



<hr>
<center>End of File</center>



