package com.softwarexpressllc.service.stat;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;

public class ClientToStatService {

	public ClientToStatService() {
		System.setProperty("javax.net.ssl.trustStore", "C:/Java/jre8/lib/security/cacerts");
		System.setProperty("javax.net.ssl.trustStorePassword", "changeit");		
		System.setProperty("javax.net.ssl.keyStore", "C:/SoftwareXpressLLC/certs/jkrych.p12");
		System.setProperty("javax.net.ssl.keyStorePassword", "");
		/**
		 * If this client used different certificate, from that of Scheduling Service client, the certificate would have to be added to
		 * Tomcat's keystore/truststore.
		 */
		
		ClientConfig clientConfig = new ClientConfig();		
		final Client client = ClientBuilder.newClient(clientConfig);		
		String authenticationUrl = "https://localhost:8443/StatService-1.0/rest";
		WebTarget webTarget = client.target(authenticationUrl).path("api").path("statistics").path("coreStat");
	     
		Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);				
		StatDataInput input = new StatDataInput();
		input.setRequestId("req-8:51am");
		float [] numbers = new float[] {2,4,6};
		input.setNumbers(numbers);
		Response response = invocationBuilder.post(Entity.json(input));
	     
	    System.out.println(response.getStatus());
	    System.out.println(response.readEntity(StatDataOutput.class));
	     
	    if(response.getStatus() == 200) {
	    	System.out.println("StatService call successfull.");
	    } else {
	    	System.out.println("Not authorized");
	    }
	}

	public static void main(String[] args) {
		new ClientToStatService();
	}

}
