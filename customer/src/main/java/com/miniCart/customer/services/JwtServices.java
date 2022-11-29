package com.miniCart.customer.services;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import com.miniCart.customer.constants.SecurityConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServices {

	public Authentication getAuthenticationFromJwt(String jwt) {
		SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));

		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
		String username = String.valueOf(claims.get("email"));
		String authorities = (String) claims.get("authorities");
		Authentication auth = new UsernamePasswordAuthenticationToken(username, null,
				AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
		return auth;
	}

}
