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

package com.example.configurationservice;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@EnableConfigServer
@SpringBootApplication
public class ConfigurationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConfigurationServiceApplication.class, args);
	}
	
	
	// http://localhost:8761/eureka/apps/PNC-CONFIG-CLIENT
	
	/*
{"application":{"name":"PNC-CONFIG-CLIENT","instance":[{"instanceId":"192.168.5.7:pnc-config-client:8098","hostName":"192.168.5.7","app":"PNC-CONFIG-CLIENT","ipAddr":"192.168.5.7","status":"UP","overriddenStatus":"UNKNOWN","port":{"$":8098,"@enabled":"true"},"securePort":{"$":443,"@enabled":"false"},"countryId":1,"dataCenterInfo":{"@class":"com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo","name":"MyOwn"},"leaseInfo":{"renewalIntervalInSecs":30,"durationInSecs":90,"registrationTimestamp":1616008644028,"lastRenewalTimestamp":1616010774604,"evictionTimestamp":0,"serviceUpTimestamp":1616008644028},"metadata":{"management.port":"8098"},"homePageUrl":"http://192.168.5.7:8098/","statusPageUrl":"http://192.168.5.7:8098/actuator/info","healthCheckUrl":"http://192.168.5.7:8098/actuator/health","vipAddress":"pnc-config-client","secureVipAddress":"pnc-config-client","isCoordinatingDiscoveryServer":"false","lastUpdatedTimestamp":"1616008644028","lastDirtyTimestamp":"1616008643649","actionType":"ADDED"},{"instanceId":"192.168.5.7:pnc-config-client:8097","hostName":"192.168.5.7","app":"PNC-CONFIG-CLIENT","ipAddr":"192.168.5.7","status":"UP","overriddenStatus":"UNKNOWN","port":{"$":8097,"@enabled":"true"},"securePort":{"$":443,"@enabled":"false"},"countryId":1,"dataCenterInfo":{"@class":"com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo","name":"MyOwn"},"leaseInfo":{"renewalIntervalInSecs":30,"durationInSecs":90,"registrationTimestamp":1616008644028,"lastRenewalTimestamp":1616010774604,"evictionTimestamp":0,"serviceUpTimestamp":1616008644028},"metadata":{"management.port":"8097"},"homePageUrl":"http://192.168.5.7:8097/","statusPageUrl":"http://192.168.5.7:8097/actuator/info","healthCheckUrl":"http://192.168.5.7:8097/actuator/health","vipAddress":"pnc-config-client","secureVipAddress":"pnc-config-client","isCoordinatingDiscoveryServer":"false","lastUpdatedTimestamp":"1616008644028","lastDirtyTimestamp":"1616008643714","actionType":"ADDED"}]}}
	*/

	
	@RestController
	class RefreshRestController {
		
	
		@RequestMapping("/refresh/{applicationName}")
		public String refreshByApplicationName(
				@PathVariable String applicationName) {
			
			
			System.out.println(">>> GET /refresh");
			
			// http://localhost:8761/eureka/apps/PNC-CONFIG-CLIENT
			
			RestTemplate restTemplate = new RestTemplate();
			
			String eurekaAppsUrl = "http://localhost:8761/eureka/apps/" ;
			
			ResponseEntity<String> response = restTemplate.getForEntity(eurekaAppsUrl + applicationName, String.class);
			
			String jsonBody = response.getBody();
			
			System.out.println(jsonBody);
			
			
			doThreadPostActuatorRefresh(jsonBody);
			
			
			return "OK";
			
		}

	
	
	private void doThreadPostActuatorRefresh(String jsonBody) {
			
		JsonParser parser = JsonParserFactory.getJsonParser();
		Map<String, Object> jsonMap = parser.parseMap(jsonBody);        
		System.out.println("jsonMap=" + jsonMap );
		
		Map<String, Object> application = (Map<String, Object>)jsonMap.get("application");
		
		System.out.println("application=" + application );
		
		List<Object> instance = (List<Object>)application.get("instance");
		
		System.out.println("instance=" + instance );
		System.out.println("instance=" + instance.getClass() );


		//ipAddr=192.168.5.7, status=UP, overriddenStatus=UNKNOWN, port={$=8098
		
		instance.forEach(instanceMap -> {
			
			System.out.println("instanceMap=" + instanceMap );
			System.out.println("instanceMap=" + instanceMap.getClass() );
			
			Map<String, Object> map = (Map<String, Object>)instanceMap;
			
			String ipAddr = (String)map.get("ipAddr");
			System.out.println("ipAddr=" + ipAddr );

			Map<String, Object> portMap = (Map<String, Object>)map.get("port");

			System.out.println("portMap=" + portMap );
			System.out.println("portMap=" + portMap.getClass() );
			
			Integer port = (Integer)portMap.get("$");
			
			System.out.println("port=" + port );

			
			doThreadPostActuatorRefresh(ipAddr, port);
			
		});

			
		}



	private void doThreadPostActuatorRefresh(String ip, Integer port) {
		
		new Thread(ip+":"+port){
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

	
	}

	
}
