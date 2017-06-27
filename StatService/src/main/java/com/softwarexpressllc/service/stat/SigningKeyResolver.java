package com.softwarexpressllc.service.stat;

import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import io.jsonwebtoken.lang.Strings;

@Service
public class SigningKeyResolver extends SigningKeyResolverAdapter {
	
	@Autowired
	ServletContext context;

	@SuppressWarnings("rawtypes")
	@Override
	public Key resolveSigningKey(JwsHeader header, Claims claims) {
		String kid = header.getKeyId();
		if (!Strings.hasText(kid)) {
			throw new JwtException("Missing required 'kid' header param in JWT with claims: " + claims);
		}
		Map<String, PublicKey> publicKeys = getPublicKeys();
		Key key = publicKeys.get(kid);
		if (key == null) {
			throw new JwtException("No public key registered for kid: " + kid + ". JWT claims: " + claims);
		}
		return key;
	}

	private Map<String, PublicKey> getPublicKeys() {
		Map<String, PublicKey> publicKeys = new HashMap<String, PublicKey>();
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		String path = context.getRealPath("WEB-INF/public-keys.json");
		try {
			File file = new File(path);
			if (file.exists() && file.canRead()) {
				//PublicCred[] publicCreds = mapper.readValue(file, PublicCred[].class);
				
				List<PublicCred> publicCreds = mapper.readValue(file, new TypeReference<List<PublicCred>>(){});
				for (int i = 0; i < publicCreds.size(); i++) {
					PublicCred publicCred = publicCreds.get(i);
					String kid = publicCred.getKid();
					String publicKeyBased64Url = publicCred.getBase64UrlPublicKey();

					byte[] byteKey = Base64.getDecoder().decode(publicKeyBased64Url);
					X509EncodedKeySpec X509publicKey = new X509EncodedKeySpec(byteKey);
					KeyFactory kf = KeyFactory.getInstance("RSA");
					PublicKey publicKey = kf.generatePublic(X509publicKey);
					publicKeys.put(kid, publicKey);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {			
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {			
			e.printStackTrace();
		}
		return publicKeys;
	}
}
