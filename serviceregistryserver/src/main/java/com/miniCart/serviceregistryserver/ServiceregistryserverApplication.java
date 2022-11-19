package com.miniCart.serviceregistryserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@RefreshScope
@EnableEurekaServer
public class ServiceregistryserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceregistryserverApplication.class, args);
	}

}
