package com.RampUp.EJAUNIV.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServer extends ResourceServerConfigurerAdapter {
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.
				authorizeRequests()
				//GETs
				.antMatchers(HttpMethod.GET, "/users", "/roles", "/customers", "/orders","/bankslippayment","/creditcardpayment").hasAuthority("ROLE_ADMIN")
				.antMatchers(HttpMethod.GET, "/users/**", "/customers/**", "/orders/**, /addresses/**").hasAuthority("ROLE_OPERATOR")
				.antMatchers(HttpMethod.GET, "/productOfferings/**", "/characteristics/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_OPERATOR")
				.antMatchers(HttpMethod.GET, "/addresses").denyAll()
				.antMatchers(HttpMethod.GET, "/addresses/{id}").hasAnyAuthority("ROLE_OPERATOR")
				//POSTs
				.antMatchers(HttpMethod.POST, "/characteristics/**", "/productOfferings/**").hasAuthority("ROLE_ADMIN")
				.antMatchers(HttpMethod.POST, "/customers", "/orders").hasAuthority("ROLE_OPERATOR")
				.antMatchers(HttpMethod.POST, "/users/signup").permitAll()

				//PUTs

				.antMatchers(HttpMethod.PUT, "/characteristics/**", "/productOfferings/**", "/users/updaterole/**").hasAuthority("ROLE_ADMIN")
				.antMatchers(HttpMethod.PUT, "/customers/{id}", "/orders/{id}", "/users/{id}").hasAuthority("ROLE_OPERATOR")
				//DELETEs
				.antMatchers(HttpMethod.DELETE, "/characteristics/**", "/productOfferings/**").hasAuthority("ROLE_ADMIN")
				.antMatchers(HttpMethod.DELETE, "/customers/{id}", "/orders/{id}", "/users/{id}").hasAuthority("ROLE_OPERATOR")
				.anyRequest()
				.authenticated();
	}





}
