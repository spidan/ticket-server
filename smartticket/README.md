# Smart Interoperability Layer

The **Smart Interoperability Layer** (InterOp Layer) acts as a flexible, light weight mapping service to allow communication between a range of different REST based web service APIs. Making use of *RML*, provided by an arbitrary remote mapping service, and the corresponding *rml mapping files*, requests are lifted to a *semantic level* from where requests for target services can be queried.

## Installation
Build the project using
```
mvn clean install
````

Then either start the generated jar on its own from the target folder as a standalone springboot service via
```
java -jar target/smartticket.jar
```

However, it is recommended to use the *docker-compose* file in the project parent folder to start the ticket service along with a suitable RML mapping service.

## Connect to a RML mapping service

In order to specify a RML mapping service to be used by the interop layer, send a POST request with the URL to be used as parameter to the endpoint
```
http://<address-and-port-of-interop-layer>/rmlService/
```
## Using the InteropLayer
The InteropLayer handles requests in the following steps:
1. Receive a request at the endpoint
```
http://<address-and-port-of-interop-layer>/ticket
```
With the body containing the request following the API expected by the *client service* and with the following additional parameters:
  1. The *name of the target service* as specified in the registry. The name is necessary to determine the endpoint to forwared the mapped request to and to get the correct *request template* from the template registry.
  2. The *file name of the mapping file* to be used to lift the incoming request on a semantic level
2. The interOp layer will forward the request to the specified RML mapping service to be mapped to turtle RDF using the specified mapping file, receive the response and store it in its internal request triple store.
3. From the internal triple store, the interopService will query the necessary information and put it into the *request template* for the target service to build a proper request for the target API.
4. The response of the target service will be received by the InterOp layer and forwarded to the initial client.

### Registering services to be called
Registering target services in the interop layer is done by sending a *ttl RDF* to the registry endpoint that describes the service to be called.
### Request Templates and Mapping Files
### Endpoints