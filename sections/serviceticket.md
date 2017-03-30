This page contains the sample payloads for Creating a ticket and Updating the same ticket.

###Create Ticket

####$batch with Deep Insert

The following payload creates a Service ticket with:
  * Ticket header
  * 2 attachments
  * 2 notes

The JSON payload itself is an example of the Deep Insert payload. In the JSON payload multiple entity types are being created together as a part of the same payload. Note that the Deep Insert payload can be directly be POST-ed to the corresponding Collection end-point.

In addition, for demonstration purposes the Deep Insert is being done via a $batch payload.

$batch payload is posted to the following end-point:
```
https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/v1/servicerequest/$batch
```

<strong>Please note that binary values must be provided as Base64 encoded!</strong>

#####Request payload
```
--batch_9116e4ce-8c77-4c55-a667-20c4076238cf
Content-Type: multipart/mixed; boundary=changeset_48b47880-84d2-4f8c-aaa9-c5bcd88ec05c

--changeset_48b47880-84d2-4f8c-aaa9-c5bcd88ec05c
Content-Type: application/http
Content-Transfer-Encoding: binary
Content-Id: 00068cbe-6a06-476f-91e6-fe46ecd3b22e

POST ServiceRequestCollection HTTP/1.1
Content-Length: 1012
Accept: application/json
Content-ID: 00068cbe-6a06-476f-91e6-fe46ecd3b22e
Content-Type: application/json

{
  "ActivityServiceIssueCategoryID": "OS-CAN-OL-IC", 
  "CauseServiceIssueCategoryID": "OS-CAN-OL", 
  "DataOriginTypeCode": "4", 
  "IncidentServiceIssueCategoryID": "OS-CAN", 
  "Name": "Ticket creation via Generic OData consumer", 
  "ProcessingTypeCode": "SRRQ", 
  "ProductID": "LENGLO-01", 
  "ReportedForPartyID": "E3300", 
  "ReporterPartyID": "E3300", 
  "ServiceIssueCategoryID": "OS", 
  "ServiceRequestAttachmentFolder": [
    {
      "Binary": "89504E470D0A1A0A0000000D494844520000010C000000740806000000708A0C8B0000000970485973000016250000162501495224F00000001C69444F540000", 
      "CategoryCode": "2", 
      "MimeType": "image/png", 
      "Name": "first.png", 
      "TypeCode": "10001"
    }, 
    {
      "Binary": "89504E470D0A1A0A0000000D494844520000010C000000740806000000708A0C8B0000000970485973000016250000162501495224F00000001C69444F540000", 
      "CategoryCode": "2", 
      "MimeType": "image/png", 
      "Name": "second.png", 
      "TypeCode": "10001"
    }
  ], 
  "ServiceRequestTextCollection": [
    {
      "Text": "Piston Rattling 1 - Generic OData Test Create", 
      "TypeCode": "10004"
    }, 
    {
      "Text": "Piston Rattling 2 - Generic OData Test Create", 
      "TypeCode": "10007"
    }
  ]
}
--changeset_48b47880-84d2-4f8c-aaa9-c5bcd88ec05c--

--batch_9116e4ce-8c77-4c55-a667-20c4076238cf--
```

#####Response from C4C

```
--ejjeeffe0
Content-Type: multipart/mixed; boundary=ejjeeffe1
Content-Length:      4615

--ejjeeffe1
Content-Type: application/http
Content-Length: 4494
Content-Transfer-Encoding: binary

HTTP/1.1 201 Created
Content-Type: application/json
Content-Length: 4214
location: https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/v1/servicerequest/ServiceRequestCollection('00163E06B9841EE592C23175596A9364')
dataserviceversion: 2.0
cache-control: no-cache, no-store

{
  "d": {
    "results": {
      "ActivityCategoryName": {
        "__metadata": {
          "type": "servicerequest.MEDIUM_Name"
        }, 
        "content": "Issued Credit", 
        "languageCode": "E"
      }, 
      "ActivityServiceIssueCategoryID": "OS-CAN-OL-IC", 
      "AddressSnapshotPostalAddress": {
        "__deferred": {
          "uri": "https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/v1/servicerequest/ServiceRequestCollection('00163E06B9841EE592C23175596A9364')/AddressSnapshotPostalAddress"
        }
      }, 
      "ApprovalStatusCode": "", 
      "ApprovalStatusCodeText": "", 
      "AssignedTo": "", 
      "CauseCategoryName": {
        "__metadata": {
          "type": "servicerequest.MEDIUM_Name"
        }, 
        "content": "Order Late", 
        "languageCode": "E"
      }, 
      "CauseServiceIssueCategoryID": "OS-CAN-OL", 
      "ChangedBy": "Eddie Smoke", 
      "CompletedOnDate": null, 
      "CompletionDueDate": "/Date(1440633600000)/", 
      "Contact": "", 
      "ContractID": "", 
      "CreatedBy": "Eddie Smoke", 
      "CreationDate": "/Date(1440374400000)/", 
      "Customer": "", 
      "CustomerID": "", 
      "DataOriginTypeCode": "4", 
      "DataOriginTypeCodeText": "Internet", 
      "ID": "3432", 
      "IncidentCategoryName": {
        "__metadata": {
          "type": "servicerequest.MEDIUM_Name"
        }, 
        "content": "Cancel Order", 
        "languageCode": "E"
      }, 
      "IncidentServiceIssueCategoryID": "OS-CAN", 
      "InitialResponseDate": null, 
      "InitialReviewDueDate": "/Date(1440374400000)/", 
      "InstallationPointID": "", 
      "InstalledBaseID": "", 
      "ItemListServiceRequestExecutionLifeCycleStatusCode": "1", 
      "ItemListServiceRequestExecutionLifeCycleStatusCodeText": "Open", 
      "LastChangeDate": "/Date(1440374400000)/", 
      "LastResponseOnDate": "/Date(1440374400000)/", 
      "Name": "Ticket creation via Generic OData consumer", 
      "NextResponseDueDate": null, 
      "ObjectCategoryName": {
        "__metadata": {
          "type": "servicerequest.MEDIUM_Name"
        }, 
        "content": "", 
        "languageCode": ""
      }, 
      "ObjectID": "00163E06B9841EE592C23175596A9364", 
      "ObjectServiceIssueCategoryID": "", 
      "Partner": "", 
      "PartnerID": "", 
      "ProcessingTypeCode": "SRRQ", 
      "ProcessingTypeCodeText": "Service Request", 
      "ProductCategoryDescription": "", 
      "ProductID": "LENGLO-01", 
      "ReferenceDate": null, 
      "ReportedForEmail": "gabriele.bodda@sap.com", 
      "ReportedForPartyID": "8000000270", 
      "ReporterEmail": "", 
      "ReporterPartyID": "E3300", 
      "RequestAssignmentStatusCode": "1", 
      "RequestAssignmentStatusCodeText": "Processor Action", 
      "RequestedEnd": "2015-08-27T00:00:00Z", 
      "RequestedEndTimeZoneCode": "UTC", 
      "RequestedStart": "2015-08-26T00:00:00Z", 
      "RequestedStartTimeZoneCode": "UTC", 
      "SalesTerritoryID": "", 
      "SerialID": "", 
      "ServiceAndSupportTeam": "S3110", 
      "ServiceCategoryName": {
        "__metadata": {
          "type": "servicerequest.MEDIUM_Name"
        }, 
        "content": "Order Support", 
        "languageCode": "E"
      }, 
      "ServiceIssueCategoryID": "OS", 
      "ServiceLevelAgreement": "CLOUDFORSERVICE_STANDARD", 
      "ServicePriorityCode": "3", 
      "ServicePriorityCodeText": "Normal", 
      "ServiceRequestAttachmentFolder": {
        "__deferred": {
          "uri": "https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/v1/servicerequest/ServiceRequestCollection('00163E06B9841EE592C23175596A9364')/ServiceRequestAttachmentFolder"
        }
      }, 
      "ServiceRequestBusinessTransactionDocumentReference": {
        "__deferred": {
          "uri": "https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/v1/servicerequest/ServiceRequestCollection('00163E06B9841EE592C23175596A9364')/ServiceRequestBusinessTransactionDocumentReference"
        }
      }, 
      "ServiceRequestClassificationCode": "", 
      "ServiceRequestClassificationCodeText": "", 
      "ServiceRequestDescription": {
        "__deferred": {
          "uri": "https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/v1/servicerequest/ServiceRequestCollection('00163E06B9841EE592C23175596A9364')/ServiceRequestDescription"
        }
      }, 
      "ServiceRequestItem": {
        "__deferred": {
          "uri": "https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/v1/servicerequest/ServiceRequestCollection('00163E06B9841EE592C23175596A9364')/ServiceRequestItem"
        }
      }, 
      "ServiceRequestLifeCycleStatusCode": "1", 
      "ServiceRequestLifeCycleStatusCodeText": "Open", 
      "ServiceRequestTextCollection": {
        "__deferred": {
          "uri": "https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/v1/servicerequest/ServiceRequestCollection('00163E06B9841EE592C23175596A9364')/ServiceRequestTextCollection"
        }
      }, 
      "ServiceRequestUserLifeCycleStatusCode": "1", 
      "ServiceRequestUserLifeCycleStatusCodeText": "Open", 
      "ServiceTechnician": "", 
      "ServiceTechnicianTeam": "", 
      "WarrantyDescription": "", 
      "WarrantyFrom": null, 
      "WarrantyTo": null, 
      "__metadata": {
        "type": "servicerequest.ServiceRequest", 
        "uri": "https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/v1/servicerequest/ServiceRequestCollection('00163E06B9841EE592C23175596A9364')"
      }
    }
  }
}
--ejjeeffe1--

--ejjeeffe0--

```

###Update (the created) ticket

The ticket, created in the previous step is now updated as follows:
  * Ticket subject is updated (using the PATCH operation)
  * 2 new notes are added
  * 2 new attachments are added

Update of multiple entities can be done only using a $batch call. As in the case of create the transaction boundary is the --changeset_921baf6c-f909-4790-8157-a2919ab7c60e.

$batch payload is posted to the following end-point (same as the create scenario):
```
https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/v1/servicerequest/$batch
```
#####Request Payload
```
--batch_1189544d-c22a-4728-aae5-5db5942ab290
Content-Type: multipart/mixed; boundary=changeset_921baf6c-f909-4790-8157-a2919ab7c60e

--changeset_921baf6c-f909-4790-8157-a2919ab7c60e
Content-Type: application/http
Content-Transfer-Encoding: binary

PATCH ServiceRequestCollection('00163E06B9841EE592C23175596A9364') HTTP/1.1
Content-Length: 392
Accept: application/json
Content-ID: 0ccafd14-217c-40ca-90f3-da0b52898274
Content-Type: application/json

{
  "ActivityServiceIssueCategoryID": "OS-CAN-OL-IC", 
  "CauseServiceIssueCategoryID": "OS-CAN-OL", 
  "DataOriginTypeCode": "4", 
  "IncidentServiceIssueCategoryID": "OS-CAN", 
  "Name": "Subject updated via update ticket OData..", 
  "ObjectID": "00163E06B9841EE592C23175596A9364", 
  "ProcessingTypeCode": "SRRQ", 
  "ProductID": "LENGLO-01", 
  "ReportedForPartyID": "E3300", 
  "ReporterPartyID": "E3300", 
  "ServiceIssueCategoryID": "OS"
}
--changeset_921baf6c-f909-4790-8157-a2919ab7c60e
Content-Type: application/http
Content-Transfer-Encoding: binary

POST ServiceRequestTextCollectionCollection HTTP/1.1
Content-Length: 110
Accept: application/json
Content-ID: 948c7e8f-88f9-453d-8b64-d3c1b9e35d75
Content-Type: application/json

{"TypeCode":"10008","Text":"1st comment from customer !!","ParentObjectID":"00163E06B9841EE592C23175596A9364"}
--changeset_921baf6c-f909-4790-8157-a2919ab7c60e
Content-Type: application/http
Content-Transfer-Encoding: binary

POST ServiceRequestTextCollectionCollection HTTP/1.1
Content-Length: 110
Accept: application/json
Content-ID: 0706dfe5-ded4-470c-a339-c024656c7489
Content-Type: application/json

{"TypeCode":"10008","Text":"2nd comment from customer !!","ParentObjectID":"00163E06B9841EE592C23175596A9364"}
--changeset_921baf6c-f909-4790-8157-a2919ab7c60e
Content-Type: application/http
Content-Transfer-Encoding: binary

POST ServiceRequestAttachmentFolderCollection HTTP/1.1
Content-Length: 273
Accept: application/json
Content-ID: b2d6a041-aadb-4d31-885f-227976b1ae02
Content-Type: application/json

{
  "Binary": "89504E470D0A1A0A0000000D494844520000010C000000740806000000708A0C8B0000000970485973000016250000162501495224F00000001C69444F540000", 
  "CategoryCode": "2", 
  "MimeType": "image/png", 
  "Name": "three.png", 
  "ParentObjectID": "00163E06B9841EE592C23175596A9364", 
  "TypeCode": "10001"
}
--changeset_921baf6c-f909-4790-8157-a2919ab7c60e
Content-Type: application/http
Content-Transfer-Encoding: binary

POST ServiceRequestAttachmentFolderCollection HTTP/1.1
Content-Length: 272
Accept: application/json
Content-ID: 41f9a621-b556-4075-831e-f7fcb10c35e0
Content-Type: application/json

{
  "Binary": "89504E470D0A1A0A0000000D494844520000010C000000740806000000708A0C8B0000000970485973000016250000162501495224F00000001C69444F540000", 
  "CategoryCode": "2", 
  "MimeType": "image/png", 
  "Name": "four.png", 
  "ParentObjectID": "00163E06B9841EE592C23175596A9364", 
  "TypeCode": "10001"
}
--changeset_921baf6c-f909-4790-8157-a2919ab7c60e--

--batch_1189544d-c22a-4728-aae5-5db5942ab290--
```

#####Response payload

```
--ejjeeffe0
Content-Type: multipart/mixed; boundary=ejjeeffe1
Content-Length:      4704

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
Content-Length: 931
Content-Transfer-Encoding: binary

HTTP/1.1 201 Created
Content-Type: application/json
Content-Length: 638
location: https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/v1/servicerequest/ServiceRequestTextCollectionCollection('00163E06B9841EE592C23242FC7A9364')
dataserviceversion: 2.0
cache-control: no-cache, no-store

{
  "d": {
    "results": {
      "AuthorName": "", 
      "AuthorUUID": null, 
      "CreatedBy": "Eddie Smoke", 
      "CreatedOn": "/Date(1440387503593)/", 
      "LanguageCode": "", 
      "LanguageCodeText": "", 
      "LastUpdatedBy": "Eddie Smoke", 
      "ObjectID": "00163E06B9841EE592C23242FC7A9364", 
      "ParentObjectID": "00163E06B9841EE592C23175596A9364", 
      "Text": "1st comment from customer !!", 
      "TypeCode": "10008", 
      "TypeCodeText": "Reply from Customer", 
      "UpdatedOn": "/Date(1440387503593)/", 
      "__metadata": {
        "type": "servicerequest.ServiceRequestTextCollection", 
        "uri": "https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/v1/servicerequest/ServiceRequestTextCollectionCollection('00163E06B9841EE592C23242FC7A9364')"
      }
    }
  }
}
--ejjeeffe1
Content-Type: application/http
Content-Length: 931
Content-Transfer-Encoding: binary

HTTP/1.1 201 Created
Content-Type: application/json
Content-Length: 638
location: https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/v1/servicerequest/ServiceRequestTextCollectionCollection('00163E06B9841EE592C23242FC7BB364')
dataserviceversion: 2.0
cache-control: no-cache, no-store

{
  "d": {
    "results": {
      "AuthorName": "", 
      "AuthorUUID": null, 
      "CreatedBy": "Eddie Smoke", 
      "CreatedOn": "/Date(1440387503593)/", 
      "LanguageCode": "", 
      "LanguageCodeText": "", 
      "LastUpdatedBy": "Eddie Smoke", 
      "ObjectID": "00163E06B9841EE592C23242FC7BB364", 
      "ParentObjectID": "00163E06B9841EE592C23175596A9364", 
      "Text": "2nd comment from customer !!", 
      "TypeCode": "10008", 
      "TypeCodeText": "Reply from Customer", 
      "UpdatedOn": "/Date(1440387503593)/", 
      "__metadata": {
        "type": "servicerequest.ServiceRequestTextCollection", 
        "uri": "https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/v1/servicerequest/ServiceRequestTextCollectionCollection('00163E06B9841EE592C23242FC7BB364')"
      }
    }
  }
}
--ejjeeffe1
Content-Type: application/http
Content-Length: 1103
Content-Transfer-Encoding: binary

HTTP/1.1 201 Created
Content-Type: application/json
Content-Length: 808
location: https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/v1/servicerequest/ServiceRequestAttachmentFolderCollection('00163E06B9841EE592C23242FC7BF364')
dataserviceversion: 2.0
cache-control: no-cache, no-store

{
  "d": {
    "results": {
      "Binary": "89504E470D0A1A0A0000000D494844520000010C000000740806000000708A0C8B0000000970485973000016250000162501495224F00000001C69444F540000", 
      "CategoryCode": "2", 
      "CategoryCodeText": "Document", 
      "CreatedBy": "Eddie Smoke", 
      "CreatedOn": "/Date(1440387503593)/", 
      "LastUpdatedBy": "Eddie Smoke", 
      "LastUpdatedOn": "/Date(1440387503442)/", 
      "LinkWebURI": "", 
      "MimeType": "image/png", 
      "Name": "three.png", 
      "ObjectID": "00163E06B9841EE592C23242FC7BF364", 
      "ParentObjectID": "00163E06B9841EE592C23175596A9364", 
      "TypeCode": "10001", 
      "TypeCodeText": "", 
      "UUID": "00163E06-B984-1EE5-92C2-3242FC7BF364", 
      "__metadata": {
        "type": "servicerequest.ServiceRequestAttachmentFolder", 
        "uri": "https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/v1/servicerequest/ServiceRequestAttachmentFolderCollection('00163E06B9841EE592C23242FC7BF364')"
      }
    }
  }
}
--ejjeeffe1
Content-Type: application/http
Content-Length: 1102
Content-Transfer-Encoding: binary

HTTP/1.1 201 Created
Content-Type: application/json
Content-Length: 807
location: https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/v1/servicerequest/ServiceRequestAttachmentFolderCollection('00163E06B9841EE592C23242FC7C3364')
dataserviceversion: 2.0
cache-control: no-cache, no-store

{
  "d": {
    "results": {
      "Binary": "89504E470D0A1A0A0000000D494844520000010C000000740806000000708A0C8B0000000970485973000016250000162501495224F00000001C69444F540000", 
      "CategoryCode": "2", 
      "CategoryCodeText": "Document", 
      "CreatedBy": "Eddie Smoke", 
      "CreatedOn": "/Date(1440387503593)/", 
      "LastUpdatedBy": "Eddie Smoke", 
      "LastUpdatedOn": "/Date(1440387503573)/", 
      "LinkWebURI": "", 
      "MimeType": "image/png", 
      "Name": "four.png", 
      "ObjectID": "00163E06B9841EE592C23242FC7C3364", 
      "ParentObjectID": "00163E06B9841EE592C23175596A9364", 
      "TypeCode": "10001", 
      "TypeCodeText": "", 
      "UUID": "00163E06-B984-1EE5-92C2-3242FC7C3364", 
      "__metadata": {
        "type": "servicerequest.ServiceRequestAttachmentFolder", 
        "uri": "https://myNNNNNN.crm.ondemand.com/sap/c4c/odata/v1/servicerequest/ServiceRequestAttachmentFolderCollection('00163E06B9841EE592C23242FC7C3364')"
      }
    }
  }
}
--ejjeeffe1--

--ejjeeffe0--
```
