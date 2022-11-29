package com.miniCart.customer.configs;

import java.util.Arrays;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.miniCart.customer.filters.ExceptionHandlerFilter;
import com.miniCart.customer.filters.JWTTokenGeneratorFilter;
import com.miniCart.customer.filters.JWTTokenValidatorFilter;
import com.miniCart.customer.services.CustomAuthProvider;

//@EnableWebSecurity(debug = true)
@EnableWebSecurity
public class DefaultSecurityConfig {

	// @formatter:off
	private String[] whiteListUrls = { 
			"/customer-service/api/v1/customer", 
			"/customer-service/api/v1/authorized",
			"/actuator/info",
			"/error" 
	};
	
	private String[] adminOnlyAccessUrls = {
			"/customer-service/api/v1/customers",
			"/customer-service/api/v1/properties",
			"/actuator/**"
	};
	
	// @formatter:on

	@Autowired
	private JWTTokenGeneratorFilter jwtGenerator;

	@Autowired
	private JWTTokenValidatorFilter jwtValidator;

	@Autowired
	private ExceptionHandlerFilter expHndlrFltr;

	@Autowired
	private RestAuthenticationEntryPoint authEntryPoint;

	@Bean
	public SecurityFilterChain mySecurityFilterChain(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.csrf()
			.disable()
			.cors()
			.configurationSource(new CorsConfigurationSource() {
				
				@Override
				public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
					CorsConfiguration config = new CorsConfiguration();
					config.setAllowedOrigins(Collections.singletonList("*"));
					config.setAllowedMethods(Collections.singletonList("*"));
	                config.setAllowCredentials(true);
	                config.setAllowedHeaders(Collections.singletonList("*"));
	                config.setExposedHeaders(Arrays.asList("Authorization"));
	                config.setMaxAge(3600L);
					return config;
				}
			})
			.and()
			.authorizeRequests()
			.antMatchers(this.whiteListUrls).permitAll()
			.antMatchers(this.adminOnlyAccessUrls).hasRole("ADMIN")
			.anyRequest().authenticated()
			.and()
			.addFilterBefore(expHndlrFltr, CorsFilter.class)
			.addFilterAfter(jwtGenerator, BasicAuthenticationFilter.class)
			.addFilterBefore(jwtValidator, BasicAuthenticationFilter.class)
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.httpBasic()
			.authenticationEntryPoint(authEntryPoint)
			.and()
			.formLogin().disable();
		// @formatter:on
		return http.build();
	}

	@Autowired
	private CustomAuthProvider authProvider;

	@Autowired
	public void bindAuthProvider(AuthenticationManagerBuilder builder) throws Exception {
		builder.authenticationProvider(this.authProvider);
	}

}
