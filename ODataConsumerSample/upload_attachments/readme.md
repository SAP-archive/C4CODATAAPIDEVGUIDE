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

