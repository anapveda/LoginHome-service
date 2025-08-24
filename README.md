+------------------+
 Client |  Browser / App   |
        +--------+---------+
                 |
   Calls via API | e.g., /orders/123
                 v
         +-------+--------+
         |   API Gateway  |  << server-side discovery
         +-------+--------+
                 |
        +--------+---------+
        | Service Registry | (Eureka / Consul)
        +------------------+
                 ^
                 |
  Services Register Themselves
   - Order Service
   - Payment Service
   - User Service
                 |
   ----------------------------------------------------
                 |
     Internal Calls between Services using Feign:
       Order Service --Feign--> Payment Service
                          |
                          Ribbon load balances
                          among Payment instances








  ==============================API GATEWAY=============================================
  API Gateway in Spring Cloud:Spring Cloud provides Spring Cloud Gateway (modern, replacing Netflix Zuul) that integrates with Eureka service discovery.  
It automatically routes requests to microservices based on service ID.
Create Spring Cloud Gateway Project

In Spring Initializr:
Select Spring Boot
Dependencies:
Spring Cloud Gateway,Eureka Discovery Client (for service discovery),Spring Boot Actuator (optional monitoring)
POM.XML:
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-gateway</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
    </dependency>
            
</dependencies>
    server:
  port: 8080   # API Gateway runs on 8080

spring:
  application:
    name: api-gateway
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true      # Enable routing using Eureka Service IDs
          lower-case-service-id: true
      routes:
        - id: order-service
          uri: lb://order-service
          predicates:
            - Path=/orders/**
        - id: payment-service
          uri: lb://payment-service
          predicates:
            - Path=/payments/**
        - id: user-service
          uri: lb://user-service
          predicates:
            - Path=/users/**
            eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/


Run Services:
Start Eureka Server (service registry)
Start microservices (Order Service, Payment Service, User Service) → they register with Eureka
Start API Gateway


Client → http://localhost:8080/orders/1 → API Gateway → Eureka lookup → Order Service instance

Browser / Mobile App
      |
      v
http://localhost:8080/orders/123
      |
      v
+-------------------+
|   API Gateway     |  (Spring Cloud Gateway)
+---------+---------+
          |
Eureka lookup (Order Service available at :9001, :9002)
          |
Load balances
          v
+--------------------+
|  Order Service 9001|
+--------------------+

Spring Cloud Gateway = easiest Java/Spring-based API gateway.
Configure routes in YAML with Eureka and load balancing.
Alternative solutions = NGINX/Envoy for polyglot, Cloud Gateways for managed solutions

      


 

