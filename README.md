# company-structure-kafka

>status: `ready`

# Run

## Run Distributed Services (postgres, pgadmin4, kafka, kafdrop, filebeat+elk)
 `docker-compose up [-d]`
## Create and Populate DB (liquibase)
 `cd company-database`

 `mvn spring-boot:run`
## Run Discovery Server (eureka)
 `cd discovery-server`

 `mvn spring-boot:run`
## Run Services 
 `cd deparment-service`

 `mvn spring-boot:run`

 `cd employee-service`
 
 `mvn spring-boot:run`

 
## Run Gateway (zuul) 
 `cd api-gateway`

 `mvn spring-boot:run`

# Access

* postgres: `localhost:5432`
* pgadmin4: `localhost:5433`
* rest-api swagger documentation: `localhost:8111/swagger-ui.html`
* department-service REST API: `localhost:8111/department-service/api/departments` or directly `localhost:8082/api/departments`
* employee-service REST API: `localhost:8111/employee-service/api/employees` or directly `localhost:8081/api/employees`
* eureka discovery server: `localhost:8761`
* kafdrop `locahost:9000`
* kibana `localhost:5601`
* elasticsearch `localhost:9200`

