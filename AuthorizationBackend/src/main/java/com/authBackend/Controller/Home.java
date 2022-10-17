package com.authBackend.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.authBackend.Services.CustomerDetailsService;
import com.authBackend.Util.JwtUtilHelper;
import com.authBackend.models.AuthenticationRequest;
import com.authBackend.models.AuthenticationResponse;

@RestController
@RequestMapping("/auth")
public class Home {

	
	@Autowired
	private AuthenticationManager authenticationManager ;
	//this variable is used to authenticate
	
	@Autowired
	private CustomerDetailsService customerDetailsService; 
	
	@Autowired
	private JwtUtilHelper jwtUtilHelper;
	//this is used for creation of token
	
	
	@GetMapping("/healthCheck")
	public ResponseEntity<Map<String,String>> healthCheck() {
		return new ResponseEntity<Map<String,String>>(Map.of("message","Audit Authorization Microservice is Active"),HttpStatus.OK);
	}
	
	
	
	//we get user name and password from client so it is post mapping
	
	@PostMapping("/token")
	public ResponseEntity<String> generateToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
		
		ResponseEntity<String> response = null;
		try {
			System.out.println("i am here");
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), 
						authenticationRequest.getPassword()));
		final UserDetails userDetails = customerDetailsService.loadUserByUsername
				(authenticationRequest.getUsername());
		
		final String jwt = jwtUtilHelper.generateToken(userDetails);
		
		response = new ResponseEntity<String>(jwt,HttpStatus.OK);
		
		
		
		}catch (Exception e) {
			e.printStackTrace();
			response = new ResponseEntity<String>("Not authorized user",HttpStatus.FORBIDDEN);
		}
		//if the try block run successfully then now we have to jwt token
		// for creating jwt token we need user details
		
		return response;
	}
	
}
