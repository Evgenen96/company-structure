# company-structure-microservices

>status: `ready`

# Run

## Run Postgres
 `docker-compose up`
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
* department-service REST API: `localhost:8111/department-service/api/departments`
* employee-service REST API: `localhost:8111/employee-service/api/employees`
* eureka discovery server: `localhost:8761`
* kafdrop `locahost:9000`

