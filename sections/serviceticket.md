This page contains the sample payloads for Creating a ticket and Updating the same ticket.

### Create Ticket

#### $batch with Deep Insert

The following payload creates a Service ticket with:
  * Ticket header
  * 2 attachments
  * 2 notes

The JSON payload itself is an example of the Deep Insert payload. In the JSON payload multiple entity types are being created together as a part of the same payload. Note that the Deep Insert payload can be directly be POST-ed to the corresponding Collection end-point.

In addition, for demonstration purposes the Deep Insert is being done via a $batch payload.

$batch payload is posted to the following end-point:
```
https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/v1/c4codata/$batch
```

<strong>Please note that binary values must be provided as Base64 encoded!</strong>

##### Complete Request (Headers + Payload)
```
POST /proxy/sap/c4c/odata/v1/c4codata/$batch HTTP/1.1
Host: odatac4ctrial.hana.ondemand.com
Authorization: <No password is required in this tenant>
x-csrf-token: 4ZiBiNTTNPkv1qQ2Vs21bA==
Content-Type: multipart/mixed; boundary=batch_1

--batch_1
Content-Type: multipart/mixed; boundary=changeset_1

--changeset_1
Content-Type: application/http
Content-Transfer-Encoding: binary

POST ServiceRequestCollection HTTP/1.1
Content-Length: 5000
Accept: application/json
Content-Type: application/json

{
   "ServicePriorityCode": "2",
  "Name": {"content": "Testing ticket creation via OData"},
  "ServiceRequestDescription": [
    {
      "Text": "Piston Rattling 1 - Generic OData Test Create", 
      "TypeCode": "10004"
    }, 
    {
      "Text": "Piston Rattling 2 - Generic OData Test Create", 
      "TypeCode": "10007"
    }
  ],
  "ServiceRequestAttachmentFolder": [
    {
      "Binary": "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", 
      "CategoryCode": "2", 
      "MimeType": "image/gif", 
      "Name": "image.gif", 
      "TypeCode": "10001"
    },
    {
      "Binary": "VGhpcw0KaXMNCmENCm11bHRpDQpsaW5lDQp0ZXh0DQpmaWxlLg==", 
      "CategoryCode": "2", 
      "MimeType": "text/plain", 
      "Name": "sample.txt", 
      "TypeCode": "10001"
    }
  ]
}
--changeset_1--

--batch_1--

```

##### Response payload

```
--ejjeeffe0
Content-Type: multipart/mixed; boundary=ejjeeffe1
Content-Length:      4969

--ejjeeffe1
Content-Type: application/http
Content-Length: 4848
Content-Transfer-Encoding: binary

HTTP/1.1 201 Created
Content-Type: application/json
Content-Length: 4518
location: https://my309307.crm.ondemand.com/sap/c4c/odata/v1/c4codata/ServiceRequestCollection('00163E1379D01ED785B015D2038B098A')
dataserviceversion: 2.0
etag: W/"datetimeoffset'2017-03-30T19%3A40%3A10.8490260Z'"
cache-control: no-cache, no-store

{"d":{"results":{"__metadata":{"uri":"https://my309307.crm.ondemand.com/sap/c4c/odata/v1/c4codata/ServiceRequestCollection('00163E1379D01ED785B015D2038B098A')","type":"c4codata.ServiceRequest","etag":"W/\"datetimeoffset'2017-03-30T19%3A40%3A10.8490260Z'\""},"CustomerMainContactPartyID":"","RequestAssignmentStatusCode":"1","CompletedOnDate":null,"ObjectID":"00163E1379D01ED785B015D2038B098A","ActivityServiceIssueCategoryID":"","ApprovalStatusCode":"1","CauseServiceIssueCategoryID":"","CompletionDueDate":"\/Date(1491004800000)\/","ContractID":"","CreatedBy":"Eddie Smoke","CreationDate":"\/Date(1490832000000)\/","Customer":"","CustomerID":"","DataOriginTypeCode":"1","ID":"5197","IncidentCategoryName":{"__metadata":{"type":"c4codata.MEDIUM_Name"},"content":""},"ActivityCategoryName":{"__metadata":{"type":"c4codata.MEDIUM_Name"},"content":""},"CauseCategoryName":{"__metadata":{"type":"c4codata.MEDIUM_Name"},"content":""},"IncidentServiceIssueCategoryID":"","InitialReviewDueDate":"\/Date(1490832000000)\/","InstallationPointID":"","InstalledBaseID":"","ItemListServiceRequestExecutionLifeCycleStatusCode":"1","LastChangeDate":"\/Date(1490832000000)\/","LastResponseOnDate":"\/Date(1490832000000)\/","Name":{"__metadata":{"type":"c4codata.EXTENDED_Name"},"languageCode":"E","content":"Testing ticket creation via OData"},"NextResponseDueDate":null,"ObjectCategoryName":{"__metadata":{"type":"c4codata.MEDIUM_Name"},"content":""},"ObjectServiceIssueCategoryID":"","Partner":"","PartnerID":"","ProcessingTypeCode":"SRRQ","ProductID":"","ReferenceDate":null,"ReportedForEmail":"","ReportedForPartyID":"","ReporterEmail":"","ReporterPartyID":"","RequestedEnd":"2017-04-02T00:00:00Z","RequestedStart":"2017-04-01T00:00:00Z","RoleCode":"8","SalesTerritoryID":"","SerialID":"","ServiceCategoryName":{"__metadata":{"type":"c4codata.MEDIUM_Name"},"content":""},"ServiceIssueCategoryID":"","ServiceLevelAgreement":"CLOUDFORSERVICE_STANDARD","ServicePriorityCode":"2","ServiceRequestClassificationCode":"","ServiceRequestLifeCycleStatusCode":"1","ServiceTechnician":"","ServiceTechnicianTeam":"","WarrantyDescription":"","WarrantyFrom":null,"WarrantyTo":null,"ServiceAndSupportTeam":"","ServiceRequestUserLifeCycleStatusCode":"1","AssignedTo":"","EscalationStatus":"1","AssignedToName":{"__metadata":{"type":"c4codata.ENCRYPTED_LONG_Name"},"languageCode":"","content":""},"ProductCategoryDescription":"","InitialResponseDate":null,"Contact":"","ParentServiceRequest":"","ETag":"\/Date(1490902810849)\/","CreationDateTime":"\/Date(1490902810849)\/","LastChangeDateTime":"\/Date(1490902810849)\/","RequestedStartTimeZoneCode":"UTC","RequestedEndTimeZoneCode":"UTC","ChangedBy":"Eddie Smoke","RequestAssignmentStatusCodeText":"Processor Action","ApprovalStatusCodeText":"Not Started","DataOriginTypeCodeText":"Manual data entry","ItemListServiceRequestExecutionLifeCycleStatusCodeText":"Open","ProcessingTypeCodeText":"Service Request","RoleCodeText":"Service Point","ServicePriorityCodeText":"Urgent","ServiceRequestClassificationCodeText":"","ServiceRequestLifeCycleStatusCodeText":"Open","ServiceRequestUserLifeCycleStatusCodeText":"Open","EscalationStatusText":"Not Escalated","RequestedStartTimeZoneCodeText":"UTC+0","RequestedEndTimeZoneCodeText":"UTC+0","ServiceRequestItem":{"__deferred":{"uri":"https://my309307.crm.ondemand.com/sap/c4c/odata/v1/c4codata/ServiceRequestCollection('00163E1379D01ED785B015D2038B098A')/ServiceRequestItem"}},"ServiceRequestAttachmentFolder":{"__deferred":{"uri":"https://my309307.crm.ondemand.com/sap/c4c/odata/v1/c4codata/ServiceRequestCollection('00163E1379D01ED785B015D2038B098A')/ServiceRequestAttachmentFolder"}},"ServiceRequestBusinessTransactionDocumentReference":{"__deferred":{"uri":"https://my309307.crm.ondemand.com/sap/c4c/odata/v1/c4codata/ServiceRequestCollection('00163E1379D01ED785B015D2038B098A')/ServiceRequestBusinessTransactionDocumentReference"}},"ServiceRequestDescription":{"__deferred":{"uri":"https://my309307.crm.ondemand.com/sap/c4c/odata/v1/c4codata/ServiceRequestCollection('00163E1379D01ED785B015D2038B098A')/ServiceRequestDescription"}},"ServicePointLocationAddress":{"__deferred":{"uri":"https://my309307.crm.ondemand.com/sap/c4c/odata/v1/c4codata/ServiceRequestCollection('00163E1379D01ED785B015D2038B098A')/ServicePointLocationAddress"}},"ServiceRequestHistoricalVersion":{"__deferred":{"uri":"https://my309307.crm.ondemand.com/sap/c4c/odata/v1/c4codata/ServiceRequestCollection('00163E1379D01ED785B015D2038B098A')/ServiceRequestHistoricalVersion"}}}}}
--ejjeeffe1--

--ejjeeffe0--

```

### Update (the created) ticket

The ticket, created in the previous step is now updated as follows:
  * Ticket subject is updated (using the PATCH operation)
  * 2 new notes are added
  * 2 new attachments are added

Update of multiple entities can be done only using a $batch call. As in the case of create the transaction boundary is the --changeset_921baf6c-f909-4790-8157-a2919ab7c60e.

$batch payload is posted to the following end-point (same as the create scenario):
```
https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/v1/c4codata/$batch
```
##### Complete Request (Headers + Payload)
```
POST /proxy/sap/c4c/odata/v1/c4codata/$batch HTTP/1.1
Host: odatac4ctrial.hana.ondemand.com
Authorization: Authorization: <No password is required in this tenant>
x-csrf-token: HYsLa5TUnzOudFM1CgJEZA==
Content-Type: multipart/mixed; boundary=batch_1

--batch_1
Content-Type: multipart/mixed; boundary=changeset_1

--changeset_1
Content-Type: application/http
Content-Transfer-Encoding: binary

PATCH ServiceRequestCollection('00163E1379D01ED785B015D2038B098A') HTTP/1.1
Content-Length: 200
Accept: application/json
Content-Type: application/json

{
  "Name": {"content": "Testing ticket creation via OData"}
}
--changeset_1
Content-Type: application/http
Content-Transfer-Encoding: binary

POST ServiceRequestDescriptionCollection HTTP/1.1
Content-Length: 200
Accept: application/json
Content-Type: application/json

{"TypeCode":"10008","Text":"1st comment from customer !!","ParentObjectID":"00163E1379D01ED785B015D2038B098A"}
--changeset_1
Content-Type: application/http
Content-Transfer-Encoding: binary

POST ServiceRequestDescriptionCollection HTTP/1.1
Content-Length: 200
Accept: application/json
Content-Type: application/json

{"TypeCode":"10008","Text":"2nd comment from customer !!","ParentObjectID":"00163E1379D01ED785B015D2038B098A"}
--changeset_1
Content-Type: application/http
Content-Transfer-Encoding: binary

POST ServiceRequestAttachmentFolderCollection HTTP/1.1
Content-Length: 300
Accept: application/json
Content-Type: application/json

{
  "Binary": "VGhpcw0KaXMNCmENCm11bHRpDQpsaW5lDQp0ZXh0DQpmaWxlLg==", 
  "CategoryCode": "2", 
  "MimeType": "text/plain", 
  "Name": "three.txt", 
  "ParentObjectID": "00163E1379D01ED785B015D2038B098A", 
  "TypeCode": "10001"
}
--changeset_1
Content-Type: application/http
Content-Transfer-Encoding: binary

POST ServiceRequestAttachmentFolderCollection HTTP/1.1
Content-Length: 3000
Accept: application/json
Content-Type: application/json

{
  "Binary": "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", 
  "CategoryCode": "2", 
  "MimeType": "image/gif", 
  "Name": "four.gif", 
  "ParentObjectID": "00163E1379D01ED785B015D2038B098A", 
  "TypeCode": "10001"
}
--changeset_1--

--batch_1--
```

##### Response payload 

```
--ejjeeffe0
Content-Type: multipart/mixed; boundary=ejjeeffe1
Content-Length:      7874

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
Content-Length: 1053
Content-Transfer-Encoding: binary

HTTP/1.1 201 Created
Content-Type: application/json
Content-Length: 713
location: https://my309307.crm.ondemand.com/sap/c4c/odata/v1/c4codata/ServiceRequestDescriptionCollection('00163E1379D01EE785B1D128A8C10224')
dataserviceversion: 2.0
etag: W/"datetimeoffset'2017-03-30T21%3A19%3A20.0009210Z'"
cache-control: no-cache, no-store

{"d":{"results":{"__metadata":{"uri":"https://my309307.crm.ondemand.com/sap/c4c/odata/v1/c4codata/ServiceRequestDescriptionCollection('00163E1379D01EE785B1D128A8C10224')","type":"c4codata.ServiceRequestDescription","etag":"W/\"datetimeoffset'2017-03-30T21%3A19%3A20.0009210Z'\""},"ParentObjectID":"00163E1379D01ED785B015D2038B098A","ObjectID":"00163E1379D01EE785B1D128A8C10224","TypeCode":"10008","LanguageCode":"","Text":"1st comment from customer !!","CreatedOn":"\/Date(1490908760000)\/","UpdatedOn":"\/Date(1490908760000)\/","CreatedBy":"Eddie Smoke","LastUpdatedBy":"Eddie Smoke","AuthorName":"","AuthorUUID":null,"ETag":"\/Date(1490908760000)\/","TypeCodeText":"Reply from Customer","LanguageCodeText":""}}}
--ejjeeffe1
Content-Type: application/http
Content-Length: 1053
Content-Transfer-Encoding: binary

HTTP/1.1 201 Created
Content-Type: application/json
Content-Length: 713
location: https://my309307.crm.ondemand.com/sap/c4c/odata/v1/c4codata/ServiceRequestDescriptionCollection('00163E1379D01EE785B1D128A8C20224')
dataserviceversion: 2.0
etag: W/"datetimeoffset'2017-03-30T21%3A19%3A20.0009210Z'"
cache-control: no-cache, no-store

{"d":{"results":{"__metadata":{"uri":"https://my309307.crm.ondemand.com/sap/c4c/odata/v1/c4codata/ServiceRequestDescriptionCollection('00163E1379D01EE785B1D128A8C20224')","type":"c4codata.ServiceRequestDescription","etag":"W/\"datetimeoffset'2017-03-30T21%3A19%3A20.0009210Z'\""},"ParentObjectID":"00163E1379D01ED785B015D2038B098A","ObjectID":"00163E1379D01EE785B1D128A8C20224","TypeCode":"10008","LanguageCode":"","Text":"2nd comment from customer !!","CreatedOn":"\/Date(1490908760000)\/","UpdatedOn":"\/Date(1490908760000)\/","CreatedBy":"Eddie Smoke","LastUpdatedBy":"Eddie Smoke","AuthorName":"","AuthorUUID":null,"ETag":"\/Date(1490908760000)\/","TypeCodeText":"Reply from Customer","LanguageCodeText":""}}}
--ejjeeffe1
Content-Type: application/http
Content-Length: 1288
Content-Transfer-Encoding: binary

HTTP/1.1 201 Created
Content-Type: application/json
Content-Length: 943
location: https://my309307.crm.ondemand.com/sap/c4c/odata/v1/c4codata/ServiceRequestAttachmentFolderCollection('00163E1379D01EE785B1D128A8C2C224')
dataserviceversion: 2.0
etag: W/"datetimeoffset'2017-03-30T21%3A19%3A20.0009210Z'"
cache-control: no-cache, no-store

{"d":{"results":{"__metadata":{"uri":"https://my309307.crm.ondemand.com/sap/c4c/odata/v1/c4codata/ServiceRequestAttachmentFolderCollection('00163E1379D01EE785B1D128A8C2C224')","type":"c4codata.ServiceRequestAttachmentFolder","etag":"W/\"datetimeoffset'2017-03-30T21%3A19%3A20.0009210Z'\""},"ObjectID":"00163E1379D01EE785B1D128A8C2C224","ParentObjectID":"00163E1379D01ED785B015D2038B098A","UUID":"00163E13-79D0-1EE7-85B1-D128A8C2C224","TypeCode":"10001","MimeType":"text/plain","Binary":"VGhpcw0KaXMNCmENCm11bHRpDQpsaW5lDQp0ZXh0DQpmaWxlLg==","DocumentLink":"https://my309307.crm.ondemand.com/sap/c4c/odata/v1/c4codata/$batch/Binary/$value","Name":"three.txt","CategoryCode":"2","LinkWebURI":"","CreatedOn":"\/Date(1490908760000)\/","LastUpdatedOn":"\/Date(1490908760000)\/","CreatedBy":"Eddie Smoke","LastUpdatedBy":"Eddie Smoke","ETag":"\/Date(1490908760000)\/","SizeInkB":"37.00000000000000","TypeCodeText":"","CategoryCodeText":"Document"}}}
--ejjeeffe1
Content-Type: application/http
Content-Length: 3841
Content-Transfer-Encoding: binary

HTTP/1.1 201 Created
Content-Type: application/json
Content-Length: 3495
location: https://my309307.crm.ondemand.com/sap/c4c/odata/v1/c4codata/ServiceRequestAttachmentFolderCollection('00163E1379D01EE785B1D128A8C38224')
dataserviceversion: 2.0
etag: W/"datetimeoffset'2017-03-30T21%3A19%3A20.0009210Z'"
cache-control: no-cache, no-store

{"d":{"results":{"__metadata":{"uri":"https://my309307.crm.ondemand.com/sap/c4c/odata/v1/c4codata/ServiceRequestAttachmentFolderCollection('00163E1379D01EE785B1D128A8C38224')","type":"c4codata.ServiceRequestAttachmentFolder","etag":"W/\"datetimeoffset'2017-03-30T21%3A19%3A20.0009210Z'\""},"ObjectID":"00163E1379D01EE785B1D128A8C38224","ParentObjectID":"00163E1379D01ED785B015D2038B098A","UUID":"00163E13-79D0-1EE7-85B1-D128A8C38224","TypeCode":"10001","MimeType":"image/gif","Binary":"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","DocumentLink":"https://my309307.crm.ondemand.com/sap/c4c/odata/v1/c4codata/$batch/Binary/$value","Name":"four.gif","CategoryCode":"2","LinkWebURI":"","CreatedOn":"\/Date(1490908760000)\/","LastUpdatedOn":"\/Date(1490908760000)\/","CreatedBy":"Eddie Smoke","LastUpdatedBy":"Eddie Smoke","ETag":"\/Date(1490908760000)\/","SizeInkB":"1951.00000000000000","TypeCodeText":"","CategoryCodeText":"Document"}}}
--ejjeeffe1--

--ejjeeffe0--
```
