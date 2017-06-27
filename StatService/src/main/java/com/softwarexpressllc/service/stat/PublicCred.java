package com.softwarexpressllc.service.stat;

public class PublicCred {
	String kid;
	String base64UrlPublicKey;
	
	public PublicCred() {
		
	}
	
	public String getKid() {
		return kid;
	}
	public void setKid(String kid) {
		this.kid = kid;
	}

	public String getBase64UrlPublicKey() {
		return base64UrlPublicKey;
	}

	public void setBase64UrlPublicKey(String base64UrlPublicKey) {
		this.base64UrlPublicKey = base64UrlPublicKey;
	}
	
}
