## SAP Cloud for Customer OData API Developer's Guide
The SAP Cloud for Customer OData API Developer’s Guide complements the [SAP Cloud for Customer OData API Reference](add link) with usage details and samples for SAP Cloud for Customer OData API in a format that is most convenient to developers. Furthermore, it also covers known restrictions and limitations of the SAP Cloud for Customer OData API.

This guide assumes that you already have some basic knowledge of [OData](http://www.odata.org). If not, you may find the following resource useful:
want to start with [SAP Cloud for Customer OData API Getting Started Guide](add link).

### Consuming SAP Sales for Customer OData APIs
In late 2014, SAP Cloud for Customer released an OData service to allow bidirectional access to SAP Cloud for Customer (C4C) data. In this blog, I will provide examples for querying, creating and updating SAP Sales for Customer data. Furthermore, I will also provide a brief introduction and additional resources for those who are new to OData protocol.

SAP Cloud for Customer OData service documentation is available in SAP online help.

What is OData protocol?
Open Data (OData) Protocol is an OASIS standard that defines best practices for building and consuming RESTful APIs. It is based on HTTP protocol and provides both metadata for the entities it exposes and their relationships. In some ways, it is similar to SQL as it provides querying options such as filtering, ordering results, support for pagination, number of records and more. It supports both XML (Atom) and JSON formats to querying and modifying data.

In the rest of this blog, I will cover some of the more commonly used OData features as part of my examples.

For more information on OData please refer to http://www.odata.org where you can find detailed documentation and tutorials.

Consuming OData Services
As a RESTful API, an OData service can easily be consumed via a web browser for retrieving data. However, in order to create or update data, a REST client is needed. Among many others, two of such tools are Advanced REST Client and Postman as extensions to Chrome Browser. Similar tools are also available for Firefox. If you prefer standalone apps you can consider SoapUI or Cocoa Rest Client. Please note that none of these tools are provided, supported nor endorsed by SAP.

SAP Cloud for Customer (C4C) OData Services
In the rest of this document, I will be using the AccountCollection of the SAP Cloud for Customer OData service. In order to repeat the examples shown below, you will need to have access to a C4C tenant. 

Service Metadata
OData service metadata is retrieved by opening the following URL.

https://<c4c_tenant>/sap/c4c/odata/v1/c4codata/$metadata - where you need to replace <c4c_tenant> with the domain of your C4C tenant (e.g. my123456.crm.ondemand.com).



OData Service Document
OData service document contains the list of OData entities (a.k.a. collections) contained within that OData service. In order to retrieve the complete list of entities included in C4C OData service, you can open the following URL in your browser.

https://<c4c_tenant>/sap/c4c/odata/v1/c4codata/ (Please note at the time of writing this blog, ‘/’ at the end of the URI is required!)



To get the same information in JSON format, you need to add $format=json into your query. (i.e. https://<c4c_tenant>/sap/c4c/odata/v1/c4codata/?format=json )


Querying Data
In order to retrieve complete list of accounts (i.e. customer records), one can simply type the following URL in a web browser where <c4c_tenant> should be replaced with your actual c4c tenant:
https://<c4c_tenant>/sap/c4c/odata/v1/c4codata/AccountCollection

Once the required username and password is provided, a result page similar to the following is seen:







Query Options
OData protocol defines a number of query options that can be used for retrieving data. Here, I will provide examples of some of the commonly used query options:

Filtering results – $filter
https://<c4c_tenant>/sap/c4c/odata/v1/c4codata/AccountCollection?$filter=AccountName eq 'Hamiltons'



Retrieving limited number of records – $top
https://<c4c_tenant>/sap/c4c/odata/v1/c4codata/AccountCollection?$top=5

(Following is the response with actual entries collapsed for brevity)


Applying several query options at once
OData query options can be concatenated with & similar to a regular HTTP GET query. 

Following example retrieves “the IDs and Names of last 5 Accounts created, as a JSON document”.
https://<c4c_tenant>/sap/c4c/odata/v1/c4codata/AccountCollection?$select=AccountID,AccountName&$top=5&$orderby=CreatedOn desc&$format=json



For a complete list of OData Version 2.0 URI conventions you can refer to http://www.odata.org/documentation/odata-version-2-0/uri-conventions/.


Reading a single Account record
In order to read a single record, an OData service should be called with the associated key field(s) value in the query URL. 

https://<c4c_tenant>/sap/c4c/odata/v1/c4codata/AccountCollection('00163E038C2E1EE299C1BB0BE9398F9B')


To receive the same result in JSON format:
https://<c4c_tenant>/sap/c4c/odata/v1/c4codata/AccountCollection('00163E038C2E1EE299C1BB0BE9398F9B')?$format=json







Reading an Associated Entity
In order to read one of the associated entities (e.g. AccountMainAddress);

https://<c4c_tenant>/sap/c4c/odata/v1/c4codata/AccountCollection('00163E038C2E1EE299C1BB0BE9398F9B')/AccountMainAddress






















Cross-Site Request Forgery (CSRF) Prevention in SAP Cloud for Customer
“Cross-Site Request Forgery (CSRF) is a type of attack that occurs when a malicious web site, email, blog, instant message, or program causes a user’s web browser to perform an unwanted action on a trusted site for which the user is currently authenticated.” 
(See more details here)

In order to prevent CSRF attacks, in C4C any modifying OData operation is required to provide a valid X-CSRF-Token as part of the HTTP request header. Required X-CSRF-Token can be requested from the C4C system for the same user, prior to the modifying OData operation. The following section describes the process of requesting and providing an X-CSRF-Token.

Requesting an X-CSRF-Token
In order to request an X-CSRF-Token, any query operation to an OData entity should have the following HTTP header and its value.

HTTP Header
Value
X-CSRF-Token
Fetch

Sample request call:

Parameter
Value
URL
https://<c4c_tenant>/sap/c4c/odata/v1/c4codata/AccountCollection
or
https://<c4c_tenant>/sap/c4c/odata/v1/c4codata/AccountCollection('00163E038C2E1EE299C1BB0BE9398F9B') 
HTTP Header
x-csrf-token:fetch

Sample HTTP Response header

content-type: application/atom+xml;type=entry; charset=utf-8
content-encoding: gzip
content-length: 1043
x-csrf-token: sf6McQCbm1SKhW2iIOdgSg==
cache-control: no-cache, no-store
dataserviceversion: 2.0



Creating a new Account record
As described in the previous section, before creating the customer record a X-CSRF-Token should be requested from the server by doing an HTTP Get (i.e. OData read or query) request. 

Sample HTTP Request (XML)
HTTP Request Parameter
Value
Method
POST
URL
https://<c4c_tenant>/sap/c4c/odata/v1/c4codata/AccountCollection 
Header
Content-Type: application/atom+xml
Header
Authorization:Base64Encoded(username:password)
Header
x-csrf-token: <a valid x-csrf-token value>
Message Body (a.k.a. payload)
<?xml version="1.0" encoding="utf-8" standalone="yes"?> 
<feed xmlns="http://www.w3.org/2005/Atom" xmlns:m="http://schemas.microsoft.com/ado/2007/08/dataservices/metadata" xmlns:d="http://schemas.microsoft.com/ado/2007/08/dataservices" xml:base="https://my306768.vlab.sapbydesign.com/sap/c4c/odata/c4codata/">
   <entry> 
     <content type="application/xml"> 
       <m:properties>
          <d:AccountName>Smith’s Bakery</d:AccountName>
          <d:CategoryCode>2</d:CategoryCode>
          <d:IndustryCode>44</d:IndustryCode>
          <d:MarketingLeadIndicator>false</d:MarketingLeadIndicator>
          <d:PrimaryContactName>John Smith</d:PrimaryContactName>
          <d:RoleCode>CRM000</d:RoleCode>
          <d:RoleCodeText>Customer</d:RoleCodeText>
        </m:properties>
     </content>
   </entry> 
</feed>


Sample HTTP Response
HTTP Response Parameter
Value
Status
201 – Created
Body (Payload)
XML representation of the created object.

Sample HTTP Request (JSON)
HTTP Request Parameter
Value
Method
POST
URL
https://<c4c_tenant>/sap/c4c/odata/v1/c4codata/AccountCollection
Header
Content-Type: application/atom+xml
Header
Accept: application/json
Header
Authorization:Base64Encoded(username:password)
Header
x-csrf-token: <a valid x-csrf-token value>
Message Body (a.k.a. payload)
{
    "AccountName": "Saglam LLC",
    "CategoryCode": "2",
    "IndustryCode": "44",
    "MarketingLeadIndicator": false,
    "RoleCode": "CRM000"
}
Sample HTTP Response
HTTP Response Parameter
Value
Status
201 – Created
Body (Payload)
JSON representation of the created object.



