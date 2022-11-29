package com.miniCart.item.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.miniCart.item.proxies.CustomerServiceProxy;

import io.jsonwebtoken.JwtException;

@Service
public class JwtServices {

	@Autowired
	private CustomerServiceProxy proxy;

	public Authentication getAuthenticationFromJwt(String jwt) throws Exception {
		ResponseEntity<UsernamePasswordAuthenticationToken> authentication = this.proxy
				.getAuthentication("Bearer " + jwt);
		if (authentication.getStatusCode() != HttpStatus.OK) {
			throw new JwtException(authentication.getBody().toString());
		}
		return authentication.getBody();
	}

}
