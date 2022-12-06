package com.miniCart.apigateway.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

	@RequestMapping("/item-service")
	public Mono<ResponseEntity<?>> itemFallbackHandler() {
		return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body("Item microservices are temporarily down. Please try after some time."));
	}

	@RequestMapping("/customer-service")
	public Mono<ResponseEntity<?>> customerFallbackHandler() {
		return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body("Customer microservices are temporarily down. Please try after some time"));
	}

	@RequestMapping("/sales-oreder-service")
	public Mono<ResponseEntity<?>> salesOrderFallbackHandler() {
		return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body("Sales-Order microservices are temporarily down. Please try after some time"));
	}

}
