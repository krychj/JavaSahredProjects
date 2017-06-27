package com.softwarexpressllc.service.stat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.RsaProvider;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


@Path("/authentication")
public class AuthenticationEndpoint {
	@Context
	HttpServletRequest currentRequest;
	
	@POST	
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/mutualSSL")	
	public Response authenticate() {		
		try {			
			String token = issueToken(null);				
			return Response.ok(token).build();			
		} catch (Exception e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}
		
	private String issueToken(String username) {		
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);
		
		KeyPair keyPair = RsaProvider.generateKeyPair();		
		PublicKey publicKey = keyPair.getPublic();
		String keyId = UUID.randomUUID().toString();		
		updatePublicKeys(keyId, publicKey);
				
		JwtBuilder builder = Jwts.builder()
				.setHeaderParam("typ", "JWT")
				.setHeaderParam("kid", keyId)
				.setIssuer("softwarexpressll.com")
				.setIssuedAt(now)				
				.setSubject("User1")				
				.claim("scope", "admin")				
				.signWith(signatureAlgorithm, keyPair.getPrivate());
		
		long expMillis = nowMillis + 1 * 60 * 60 * 1000;
		Date exp = new Date(expMillis);
		builder.setExpiration(exp);
		String token = builder.compact();		
		return "Bearer " + token;		
	}
	
	void updatePublicKeys (String kid, PublicKey publicKey) {		
		String path = currentRequest.getServletContext().getRealPath("WEB-INF/public-keys.json");		
		try {
			File file = new File(path);
			if(!file.exists()) {								
				file.createNewFile();
				file.setReadable(true, true);
				file.setWritable(true, true);
			}
			if(file.canRead() && file.canWrite()) {
				String base64UrlPublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
				PublicCred pubCred = new PublicCred();
				pubCred.setKid(kid);
				pubCred.setBase64UrlPublicKey(base64UrlPublicKey);
				
				ObjectMapper mapper = new ObjectMapper();		        
				OutputStream outputStream = new FileOutputStream(file, true);
				JsonGenerator g = mapper.getFactory().createGenerator(outputStream, JsonEncoding.UTF8);
				mapper.enable(SerializationFeature.INDENT_OUTPUT);
				mapper.writerWithDefaultPrettyPrinter().writeValue(g, pubCred);
				g.close();
				outputStream.close();				
			}						
		} catch (IOException e) {			
			e.printStackTrace();
		}			
	}
}