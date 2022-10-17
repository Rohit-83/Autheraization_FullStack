package com.authBackend.Configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import com.authBackend.Services.CustomerDetailsService;


@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfiguration  {
	
	@Autowired
	private CustomerDetailsService customerUserDetailService;


    //this method contain the trick of stating  user name and password . 
    
    /**
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(customerUserDetailService);
		//this method usually have parameter of userDetailService which contain 
		//method loadByUserName.
	}
	**/
   
    
    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customerUserDetailService);
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }
	
	
	//this method tells the property of Url which have to keep safe or public
	 @Bean
	 public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		
		http
			.csrf() 
			.disable()
			.authorizeRequests()
			.antMatchers("/auth/token","/auth/healthCheck").permitAll()
			.anyRequest().authenticated()
			.and()
			
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http.authenticationProvider(daoAuthenticationProvider());
		DefaultSecurityFilterChain defaultSecurityFilterChain = http.build();
		return defaultSecurityFilterChain;
	}


    //this method is used to save the password securely in the storage
    @Bean
    PasswordEncoder passwordEncoder() {
        return  NoOpPasswordEncoder.getInstance();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
	
	
	
}
