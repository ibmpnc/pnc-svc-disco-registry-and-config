/*
 * Copyright 2012-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.configurationclient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class ConfigurationClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigurationClientApplication.class, args);
	}
}



@RefreshScope
@RestController
class MessageRestController {

	@Autowired
	private DiscoveryClient discoveryClient;

	@Value("${greeter.message}")
    private String greeterMessageFormat; 
 
    @GetMapping("/greet/{user}")
    public String greet(@PathVariable("user") String user) {
        String prefix = System.getenv().getOrDefault("GREETING_PREFIX", "Hi");
        System.out.println("Prefix :"+prefix+" and User:" + user);
        if (prefix == null) {
            prefix = "Hello!";
        }
 
        return String.format(greeterMessageFormat, prefix, user);
    }
	@RequestMapping("/service-instances/{applicationName}")
	public List<ServiceInstance> serviceInstancesByApplicationName(
			@PathVariable String applicationName) {
		return this.discoveryClient.getInstances(applicationName);
	}


	@RequestMapping("/refresh/{applicationName}")
	public String refreshByApplicationName(
			@PathVariable String applicationName) {
		
		
		List<ServiceInstance> serviceInstances = this.discoveryClient.getInstances(applicationName);
		
		serviceInstances.forEach(serviceInstance -> {
		    
			System.out.println("serviceInstance :" + serviceInstance);
		    
			doThreadPostActuatorRefresh(serviceInstance);
			
		});
		
		
		return "OK";
		
	}

	
	
	private void doThreadPostActuatorRefresh(ServiceInstance serviceInstance) {
		
		new Thread(serviceInstance.getHost()+":"+serviceInstance.getPort()){
	        public void run(){
	          
	        	
	        	System.out.println("Thread: " + getName() + " running");
	        	
	        	String hostPort = getName();
	        	
	        	doPostActuatorRefresh(hostPort);
	        	
	        	
	        }

			private void doPostActuatorRefresh(String hostPort) {
				
				RestTemplate restTemplate = new RestTemplate();
			     
			    final String baseUrl = "http://"+hostPort+"/actuator/refresh";
			    URI uri;
				try {
					
					uri = new URI(baseUrl);
			 
					HttpHeaders headers = new HttpHeaders();
				    headers.set("Content-Type","application/json");
				 
				    HttpEntity<String> request = new HttpEntity<>("", headers);
				     
				    ResponseEntity<String> result = restTemplate.postForEntity(uri, request, String.class);
				
					System.out.println("POST result :" + result);
					
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}

				
			}
			
	      }.start();
		
	}



	@Value("${message:Hello default}")
	private String message;

	@RequestMapping("/message")
	String getMessage() {
		return this.message;
	}
	
}
