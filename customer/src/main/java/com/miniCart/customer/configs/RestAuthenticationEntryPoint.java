package com.miniCart.customer.configs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniCart.customer.models.FilterErrorResponse;

@Component("restAuthenticationEntryPoint")
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authenticationException) throws IOException, ServletException {

		FilterErrorResponse errorResponse = new FilterErrorResponse();
		errorResponse.setMsg(authenticationException.getLocalizedMessage());
		errorResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setHeader("Content-Type", "application/json");
		response.getWriter().write(convertObjectToJson(errorResponse));

	}

	private String convertObjectToJson(Object object) throws JsonProcessingException {
		if (object == null) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}
}