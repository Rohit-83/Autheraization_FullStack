package com.authBackend.Util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

//this class will use for generating jwt token
//validating checking if token expires


@Component
public class JwtUtilHelper {
	
	private String SECRET_KEY = "secret";
	
	public String extractUserName(String token) {
		final Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}
	
	public Boolean isTookenExpired(String token) {
		final Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
		return claims.getExpiration().before(new Date());
	}
	
	
	public Boolean validateJwtToken(String token,UserDetails userDetails) {
		String userName = extractUserName(token);
		
		//now we will check that user name is equal or not and token is expired or not
		return (userName.equals(userDetails.getUsername()) && !isTookenExpired(token));
	}
	
	
	public String generateToken(UserDetails userDetails) {
		//we will create claims 
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims,userDetails.getUsername());
	}
	
	//now we will createToken method
	public String createToken(Map<String,Object> claims, String userName) {
		
		return Jwts.builder().setClaims(claims).setSubject(userName).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+5*60*60))
				.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
		
				
	}
	
}
