package com.miniCart.customer.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniCart.customer.models.FilterErrorResponse;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {
			filterChain.doFilter(request, response);
		} catch (Exception e) {

			SecurityContextHolder.clearContext();

			FilterErrorResponse errorResponse = new FilterErrorResponse();
			errorResponse.setMsg(e.getLocalizedMessage());
			errorResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setHeader("Content-Type", "application/json");
			response.getWriter().write(convertObjectToJson(errorResponse));
		}

	}

	private String convertObjectToJson(Object object) throws JsonProcessingException {
		if (object == null) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}

}