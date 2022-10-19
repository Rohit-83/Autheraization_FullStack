package com.authBackend.filter;

import java.io.IOException;

import javax.management.loading.PrivateClassLoader;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.objenesis.instantiator.basic.NewInstanceInstantiator;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.authBackend.Services.CustomerDetailsService;
import com.authBackend.Util.JwtUtilHelper;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtUtilHelper jwtUtilHelper;
	
	@Autowired
	private CustomerDetailsService customerDetailsService;
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		
		//first we should get header from the request
		String tokenHeader = request.getHeader("Authorization");
		
		//we will get both this from the token header
		String username = null;
		String jwtToken = null ;
		
		
		//now we will first check the token header that we get is null or wrong
		if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
			
			//if both are correct then we will take out token from token header
			jwtToken = tokenHeader.substring(7);
			
			//now for getting the user name we would try and catch
			
			try {
				username = jwtUtilHelper.getUsernameFromToken(jwtToken);
				
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			
			
			//now we validate the user name and security
			if (username != null  && SecurityContextHolder.getContext().getAuthentication() == null) {
				//first we get the user details
				
				UserDetails userDetails = customerDetailsService.loadUserByUsername(username);
				
				
				//now we will validate the user name in this area
				if (jwtUtilHelper.validateToken(jwtToken, userDetails)) {
					
					 UsernamePasswordAuthenticationToken userToken = new  UsernamePasswordAuthenticationToken
							 (userDetails,null,userDetails.getAuthorities());
					 
					 //now we have to set details 
					 userToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					 
					 SecurityContextHolder.getContext().setAuthentication(userToken);
				}
				
				
				
			}
			else {
				System.out.println("Username is not validated");
			}
			
			//filterChain.doFilter(request, response);
			
		}
		filterChain.doFilter(request, response);
		
		
	}
	
	
	
}
