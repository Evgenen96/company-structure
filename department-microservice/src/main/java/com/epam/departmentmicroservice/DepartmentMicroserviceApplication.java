package com.epam.departmentmicroservice;

import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
@EnableEurekaClient
@EnableHystrix
@EnableCircuitBreaker
public class DepartmentMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DepartmentMicroserviceApplication.class, args);
	}


	@Bean
	Logger.Level feignLoggerLevel() {
		return Logger.Level.BASIC;
	}
}
