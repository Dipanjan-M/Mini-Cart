package com.miniCart.item.proxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.miniCart.item.constants.SecurityConstants;

@FeignClient(value = "customer")
public interface CustomerServiceProxy {

	static final String COMMON_URL_PART = "/customer-service/api/v1";

	@RequestMapping(method = RequestMethod.GET, value = COMMON_URL_PART + "/authorized", consumes = "application/json")
	public ResponseEntity<UsernamePasswordAuthenticationToken> getAuthentication(
			@RequestHeader(SecurityConstants.X_REFERENCE_JWT_HEADER) String bearerToken);

}
