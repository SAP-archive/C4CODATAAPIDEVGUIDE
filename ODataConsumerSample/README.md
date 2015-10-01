OData Consumer Sample
=====================

Sample OData Consumer using Apache Olingo (v2)

This sample app demonstrates the usage of Apache Olingo (v2) to consume OData services from SAP Cloud for customer. The following scenarios are demonstrated:

* Read a list of entities using a $top and $orderBy conditions
* Create a new entity and a related entity using $batch
* Read the created entity using a $filter condition
* Update the entity using $batch
* Read the updated entity

In the class ServiceTicketODataConsumer.java please maintain the _HOST_NAME_ and _SERVICE_NAME_ to point to your service end-point in the method getODataServiceUrl(). Also maintain the _USER_NAME_ and _PASSWORD_ in the method getODataServiceUrl(). Note that the system query option criteria, entity names, entity collection names etc need to be adjusted according to your service definition. 

This sample demonstrates the usage of OData call using Basic authentication. An alternative approach is to use OAuth SAML bearer flow which allows single sign on for OData calls.

