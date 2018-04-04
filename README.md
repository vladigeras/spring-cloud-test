# Description 
This is a sample project with use Spring Cloud technology: Spring Cloud Netflix Eureka (server and clients)

# How to use
1. Clone this project
2. Open project in your favourite IDE
3. Run eureka-server module, eureka-client-1 module and eureka-client-2 module separately
4. Go to **localhost:8000** and you will see, that all servers are registered in Eureka Server Registry
5. Enjoy!

## Features
_**Eureka Registry Feature**_: client 1 available in **localhost:8001** and client 2 available in **localhost:8002**. 
They can watch to each other.

**_Load balancer Netflix Ribbon Feature:_** client 2 (**localhost:8002**) could have a replicas (copies), which available as
**localhost:8022** and **localhost:8222**. Client 1 (**localhost:8001**) have a load balancer Ribbon. You can see example, where 
Ribbon choose a replica of client 2 to run a request.

**_Spring Sleuth Feature:_** clients use Sleuth technology to logging example. Sleuth turn on additional info about 
incoming message.
