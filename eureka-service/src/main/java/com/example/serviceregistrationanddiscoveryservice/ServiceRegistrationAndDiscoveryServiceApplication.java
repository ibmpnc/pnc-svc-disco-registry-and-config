package com.example.serviceregistrationanddiscoveryservice;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.eureka.server.EurekaController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@EnableEurekaServer
@SpringBootApplication
public class ServiceRegistrationAndDiscoveryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceRegistrationAndDiscoveryServiceApplication.class, args);
	}
	
	@RequestMapping("/eurekaserver/test/{applicationName}")
	public String refreshByApplicationName(
			@PathVariable String applicationName) {
		

		System.out.println("Eureka Server /eurekaserver/test/" + applicationName );
		
		
		return "OK";
		
	}

	
	
}
