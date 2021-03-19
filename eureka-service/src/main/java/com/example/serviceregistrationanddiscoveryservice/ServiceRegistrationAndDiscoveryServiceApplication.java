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
	
	@RequestMapping("/refresh/{applicationName}")
	public String refreshByApplicationName(
			@PathVariable String applicationName) {
		
		
		
		
//		List<ServiceInstance> serviceInstances = this.discoveryClient.getInstances(applicationName);
//		
//		serviceInstances.forEach(serviceInstance -> {
//		    
//			System.out.println("serviceInstance :" + serviceInstance);
//		    
//			doThreadPostActuatorRefresh(serviceInstance);
//			
//		});
//		

		
		
		return "OK";
		
	}

	
	
}
