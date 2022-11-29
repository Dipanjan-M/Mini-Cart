package com.miniCart.item.filters;

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

import com.miniCart.item.constants.SecurityConstants;
import com.miniCart.item.services.JwtServices;

@Component
public class JwtTokenValidatorFilter extends OncePerRequestFilter {

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return request.getServletPath().equals("/item-service/api/v1/status")
				|| request.getServletPath().equals("/actuator/info");
	}

	@Autowired
	private JwtServices jwtSvcs;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String jwt = request.getHeader(SecurityConstants.JWT_HEADER);
		if (null != jwt) {
			try {
				Authentication auth = this.jwtSvcs.getAuthenticationFromJwt(jwt);
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

}
