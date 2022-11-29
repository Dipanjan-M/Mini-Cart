package com.miniCart.customer.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.miniCart.customer.constants.SecurityConstants;
import com.miniCart.customer.services.JwtServices;

@Component
public class JWTTokenValidatorFilter extends OncePerRequestFilter {

	@Autowired
	private JwtServices jwtSvcs;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String jwt = request.getHeader(SecurityConstants.JWT_HEADER);
		if (null != jwt) {
			Authentication auth;
			try {
				auth = this.jwtSvcs.getAuthenticationFromJwt(jwt);
				SecurityContextHolder.getContext().setAuthentication(auth);
				filterChain.doFilter(request, response);
			} catch (Exception e) {
				SecurityContextHolder.clearContext();
				throw new ServletException(e.getLocalizedMessage());
			}
		} else {
			throw new ServletException("Empty Authorization header, couldn't authenticate the request");
		}
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		return request.getServletPath().equals("/customer-service/api/v1/authorize")
				|| request.getServletPath().equals("/customer-service/api/v1/authorized")
				|| request.getServletPath().equals("/customer-service/api/v1/customer")
				|| request.getServletPath().equals("/actuator/info");
	}

}
