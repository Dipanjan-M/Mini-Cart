package com.miniCart.salesorder.configs;

import java.util.Arrays;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.miniCart.salesorder.filters.ExceptionHandlerFilter;
import com.miniCart.salesorder.filters.JwtTokenValidatorFilter;

@EnableWebSecurity
public class BasicSecurityConfig {

	// @formatter:off
	private String[] whiteListUrls = {
			"/h2-console/**",
			"/actuator/info",
			"/sales-order-service/api/v1/item/status",
			"/error"
	};
	
	private String[] adminOnlyAccessUrls = {
			"/actuator/**",
			"/sales-order-service/api/v1/properties",
			"/sales-order-service/api/v1/customers-sos"
	};
	
	// @formatter:on

	@Autowired
	private JwtTokenValidatorFilter jwtValidatorFilter;

	@Autowired
	private ExceptionHandlerFilter expHndlrFltr;

	@Bean
	public SecurityFilterChain mySecurityFilterChain(HttpSecurity http) throws Exception {
		// @formatter:off
		http
			.csrf().disable()
			.cors()
			.configurationSource(new CorsConfigurationSource() {
				
				@Override
				public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
					CorsConfiguration config = new CorsConfiguration();
					config.setAllowedOriginPatterns(Collections.singletonList("*"));
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
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.addFilterBefore(expHndlrFltr, CorsFilter.class)
			.addFilterBefore(jwtValidatorFilter, BasicAuthenticationFilter.class)
			.httpBasic().and()
			.formLogin().disable();
		// @formatter:on
		http.headers().frameOptions().disable();
		return http.build();
	}

}
