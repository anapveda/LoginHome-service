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
