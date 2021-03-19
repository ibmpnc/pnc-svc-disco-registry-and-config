package com.example.configurationservice;

import java.util.List;
import java.util.Map;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;

public class XPathTest {

	public static void main(String args[]) throws Exception {

		//String xml = "<resp><status>good</status><msg>hi</msg></resp>";
		String json = "{\"application\":{\"name\":\"PNC-CONFIG-CLIENT\",\"instance\":[{\"instanceId\":\"192.168.5.7:pnc-config-client:8098\",\"hostName\":\"192.168.5.7\",\"app\":\"PNC-CONFIG-CLIENT\",\"ipAddr\":\"192.168.5.7\",\"status\":\"UP\",\"overriddenStatus\":\"UNKNOWN\",\"port\":{\"$\":8098,\"@enabled\":\"true\"},\"securePort\":{\"$\":443,\"@enabled\":\"false\"},\"countryId\":1,\"dataCenterInfo\":{\"@class\":\"com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo\",\"name\":\"MyOwn\"},\"leaseInfo\":{\"renewalIntervalInSecs\":30,\"durationInSecs\":90,\"registrationTimestamp\":1616008644028,\"lastRenewalTimestamp\":1616010774604,\"evictionTimestamp\":0,\"serviceUpTimestamp\":1616008644028},\"metadata\":{\"management.port\":\"8098\"},\"homePageUrl\":\"http://192.168.5.7:8098/\",\"statusPageUrl\":\"http://192.168.5.7:8098/actuator/info\",\"healthCheckUrl\":\"http://192.168.5.7:8098/actuator/health\",\"vipAddress\":\"pnc-config-client\",\"secureVipAddress\":\"pnc-config-client\",\"isCoordinatingDiscoveryServer\":\"false\",\"lastUpdatedTimestamp\":\"1616008644028\",\"lastDirtyTimestamp\":\"1616008643649\",\"actionType\":\"ADDED\"},{\"instanceId\":\"192.168.5.7:pnc-config-client:8097\",\"hostName\":\"192.168.5.7\",\"app\":\"PNC-CONFIG-CLIENT\",\"ipAddr\":\"192.168.5.7\",\"status\":\"UP\",\"overriddenStatus\":\"UNKNOWN\",\"port\":{\"$\":8097,\"@enabled\":\"true\"},\"securePort\":{\"$\":443,\"@enabled\":\"false\"},\"countryId\":1,\"dataCenterInfo\":{\"@class\":\"com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo\",\"name\":\"MyOwn\"},\"leaseInfo\":{\"renewalIntervalInSecs\":30,\"durationInSecs\":90,\"registrationTimestamp\":1616008644028,\"lastRenewalTimestamp\":1616010774604,\"evictionTimestamp\":0,\"serviceUpTimestamp\":1616008644028},\"metadata\":{\"management.port\":\"8097\"},\"homePageUrl\":\"http://192.168.5.7:8097/\",\"statusPageUrl\":\"http://192.168.5.7:8097/actuator/info\",\"healthCheckUrl\":\"http://192.168.5.7:8097/actuator/health\",\"vipAddress\":\"pnc-config-client\",\"secureVipAddress\":\"pnc-config-client\",\"isCoordinatingDiscoveryServer\":\"false\",\"lastUpdatedTimestamp\":\"1616008644028\",\"lastDirtyTimestamp\":\"1616008643714\",\"actionType\":\"ADDED\"}]}}\n"
		;

		JsonParser parser = JsonParserFactory.getJsonParser();
		Map<String, Object> jsonMap = parser.parseMap(json);        
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

			
		});
		
		
	}
}
