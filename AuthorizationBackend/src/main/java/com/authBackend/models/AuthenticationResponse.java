package com.authBackend.models;

public class AuthenticationResponse {

	
	private  String jwtToken ;

	//Response Format
	
	public AuthenticationResponse(String jwtToken) {
		this.jwtToken = jwtToken;
	}


	public String getJwtToken() {
		return jwtToken;
	}


	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}


	public AuthenticationResponse() {
	}
	
	
	
	
}
