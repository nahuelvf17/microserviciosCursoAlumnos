package com.proyecto.microservicios.app.gateway;

import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableEurekaClient
@SpringBootApplication
public class MicroserviciosGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviciosGatewayApplication.class, args);
	}

}
