Outlined here is a general pattern for doing a mass query using OData. 

#### URL based approach

Outlined below is the simple (URL based) approach to perform an OData query, on an entity collection, using the standard system query options.

```
GET /CustomerCollection?$inlinecount=allpages&$format=json&$filter=ID eq '12345' or ID eq '567879'...
```

In some situations, where larger number of $filter conditions, this approach might not work due to URL length restrictions. In these situations the best approach is to use the $batch to do a mass GET.

#### $batch approach

In the $batch approach the GET request is part of the $batch body instead of it being part of the URL. This means that there are no length restrictions involved, consequently it is possible to send a larget GET request to do mass data query. Sample payload would be as follows:

```
--batch_875d183d-1b52-4c89-939b-95a731b38f13
Content-Type: application/http
Content-Transfer-Encoding: binary

GET CustomerCollection?$top=5000&$inlinecount=allpages&$format=json&$filter=ID eq '1000040' or ID eq '10000963' or ID eq '99003001' or ID eq '10001290' or ID eq '10001291' or ID eq '10001292' or ID eq '10001293' or ID eq '10001294' or ID eq '10001295' or ID eq '10001296' or ID eq '10001297' or ID eq '10001298' or ID eq '300002049' or ID eq '10001301'..... HTTP/1.1


--batch_875d183d-1b52-4c89-939b-95a731b38f13--
```
In the above example 5000 entries are being read with the $filter condition having 5000 OR conditions that are based on the ID attribute.

Note that before the GET in the batch body there should be one carriage return line feed and after the GET there should be at least 2 carriage return line feeds. In addition, __please note that the query parameters in the $batch body needs to be URL encoded.__

