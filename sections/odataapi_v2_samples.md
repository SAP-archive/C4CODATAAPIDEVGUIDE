# Sample calls on the SAP Cloud for Customer OData API V2
## The following calls demonstrate the creation and the updating of a ServiceRequest (ticket) 
* Deep-insert - Create a ticket with two items
* $batch update - Update the ticket header and an item in a single round-trip
 
### Deep-insert - Create a ticket with two items

```
POST /sap/c4c/odata/v1/c4codataapi/ServiceRequestCollection HTTP/1.1
Host: my<tenant_id>.crm.ondemand.com
Authorization: Basic <>
x-csrf-token: <Fetch with a prior GET call>
Content-Type: application/json
 
{
  "Name": "Testing ticket creation via OData",
  "ServiceRequestItem": [
    {
                "ProductID":"VIP_SERVICE",
                "Description": "VIP Service"
    },
    {
                "ProductID":"ECO_SERVICE",
                "Description": "Economy Service"
    }
  ]
}
``` 
 
### $batch update - Update the ticket header and an item in a single round-trip
```
POST /sap/c4c/odata/v1/c4codataapi/$batch HTTP/1.1
Host: my<tenandId>.crm.ondemand.com
Authorization: Basic <>
x-csrf-token: <fetched in a prior get call>
Content-Type: multipart/mixed; boundary=batch_1
 
--batch_1
Content-Type: multipart/mixed; boundary=changeset_1
 
--changeset_1
Content-Type: application/http
Content-Transfer-Encoding: binary
 
PATCH ServiceRequestCollection('ValidObjectID') HTTP/1.1
Content-Length: 5000
Accept: application/json
Content-Type: application/json
 
{
  "Name": "Testing ticket updated via OData"
}
--changeset_1
 
--batch_1
Content-Type: multipart/mixed; boundary=changeset_1
 
--changeset_1
Content-Type: application/http
Content-Transfer-Encoding: binary
 
PATCH ServiceRequestItemCollection('ValidObjectID-same as above') HTTP/1.1
Content-Length: 5000
Accept: application/json
Content-Type: application/json
 
{
  "Description": "VIP Service Updated"
}
--changeset_1--
 
--batch_1--
 ```
