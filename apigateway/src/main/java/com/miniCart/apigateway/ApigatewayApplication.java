package com.miniCart.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

@SpringBootApplication
@EnableEurekaClient
public class ApigatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApigatewayApplication.class, args);
	}

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		// @formatter:off
		return builder
				.routes()
					.route(p -> p.path("/mini-cart/customer/**")
						.filters((f) -> f
							.circuitBreaker(c -> c.setName("customerSvcCB").setFallbackUri("/fallback/customer-service"))
							.rewritePath("/mini-cart/customer/(?<segment>.*)", "/customer-service/api/v1/${segment}")
							.removeRequestHeader("Cookie")
							.dedupeResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "RETAIN_FIRST")
							.dedupeResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "RETAIN_FIRST")
						).uri("lb://CUSTOMER")
					)
					.route(p -> p.path("/mini-cart/item/**")
						.filters((f) -> f
							.circuitBreaker(c -> c.setName("itemSvcCB").setFallbackUri("/fallback/item-service"))
							.rewritePath("/mini-cart/item/(?<segment>.*)", "/item-service/api/v1/${segment}")
							.removeRequestHeader("Cookie")
							.dedupeResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "RETAIN_FIRST")
							.dedupeResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "RETAIN_FIRST")
						).uri("lb://ITEM")
					)
					.route(p -> p.path("/mini-cart/sales-order/**")
						.filters((f) -> f
							.circuitBreaker(c -> c.setName("salesOrderSvcCB").setFallbackUri("/fallback/sales-oreder-service"))
							.rewritePath("/mini-cart/sales-order/(?<segment>.*)", "/sales-order-service/api/v1/${segment}")
							.removeRequestHeader("Cookie")
							.dedupeResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "RETAIN_FIRST")
							.dedupeResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "RETAIN_FIRST")
						).uri("lb://SALESORDER")
					)
				.build();
		// @formatter:on
	}

}
