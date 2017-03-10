Sample OData Consumer Projects
=====================

<h2>Projects</h2>

<a href="#odata">odata</a><br>
<a href="#upload_attachments">upload_attachments</a><br>
<a href="#CSharpSample">C# Sample</a>


<h2 id="odata">odata</h2>

Sample OData Consumer using Apache Olingo (v2)

This sample app demonstrates the usage of Apache Olingo (v2) to consume OData services from SAP Cloud for customer. The following scenarios are demonstrated:

* Read a list of entities using a $top and $orderBy conditions
* Create a new entity and a related entity using $batch
* Read the created entity using a $filter condition
* Update the entity using $batch
* Read the updated entity

<b>Notes:</b>

In the class ServiceTicketODataConsumer.java please maintain the _HOST_NAME_ and _SERVICE_NAME_ to point to your service end-point in the method getODataServiceUrl(). Also maintain the _USER_NAME_ and _PASSWORD_ in the method getODataServiceUrl(). Note that the system query option criteria, entity names, entity collection names etc need to be adjusted according to your service definition. 

This sample demonstrates the usage of OData call using Basic authentication. An alternative approach is to use OAuth SAML bearer flow which allows single sign on for OData calls.


<h2 id="upload_attachments">upload_attachments</h2>

Sample OData Consumer to upload attachments to Customer Records via the C4C OData API AccountAttachment Collection.

This sample application features the following:

* Read a manifest file _manifest.csv_ containing attachment info (see more details below)
* Creates a new attachment entity for the Account indicated in the manifest file (ParentObjectID) via $batch
* Progress is logged in a file and displayed on the console

<b>Notes:</b>

Please make sure to provide the parameters listed in the _settings.properties_ file. 

In order to run the application in Eclipse, create a run or debug configuration with the following:

Under Program Arguments: -p \<your_password\>

Under VM Parameters: -Djava.util.logging.SimpleFormatter.format='%1$tY-%1$tm-%1$td %1$tH:%1$tM:%1$tS %4$s: %5$s%6$s%n' (Reformats the log output to a single line)

<b>Format of the manifest.csv File</b>:

|Column Name|Description|
|-----------|------------|
|ParentObjectID|ParentID of property of the customer record that the attachment will be linked|
|TypeCode| Required code for creating attachments - Check your C4C system for valid values|
|AttachmentName| Name of the attachment|
|MimeType| Attachment document type, e.g. application/pdf|
|Path|relative path for the folder where the manifest.csv and the attachment files are located|


<h2 id="CSharpSample">C# Sample</h2>

This sample app demonstrates reading and creating new records in C4C OData API in C#. The works around some of the issues around handling cookies and passing credentials that have been reported by partners.
