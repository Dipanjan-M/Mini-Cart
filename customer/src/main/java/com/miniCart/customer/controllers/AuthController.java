package com.miniCart.customer.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.miniCart.customer.constants.SecurityConstants;
import com.miniCart.customer.entities.Customer;
import com.miniCart.customer.models.AuthRes;
import com.miniCart.customer.repos.CustomerRepository;
import com.miniCart.customer.services.JwtServices;

@RestController
@RequestMapping("/customer-service/api/v1")
public class AuthController {

	@Autowired
	private JwtServices jwtSvcs;

	@Autowired
	private CustomerRepository custRepo;

	@GetMapping("/authorized")
	public ResponseEntity<?> checkTokenValidation(
			@RequestHeader(SecurityConstants.X_REFERENCE_JWT_HEADER) String bearerToken) {
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			String jwt = bearerToken.substring(7);
			try {
				Authentication authentication = this.jwtSvcs.getAuthenticationFromJwt(jwt);
				return ResponseEntity.ok(authentication);
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getLocalizedMessage());
			}
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Expecting a valid bearer token, received nothing");
	}

	@GetMapping("/authorize")
	public ResponseEntity<?> getUserDetailsAfterLogin(Authentication authentication) {
		Customer customer = this.custRepo.findByEmail(authentication.getName()).get();
		AuthRes authRes = new AuthRes();
		authRes.setId(customer.getId());
		authRes.setFirstName(customer.getFirstName());
		authRes.setLastName(customer.getLastName());
		authRes.setEmail(customer.getEmail());
		return ResponseEntity.ok(authRes);
	}

}
