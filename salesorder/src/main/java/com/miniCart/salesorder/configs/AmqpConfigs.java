package com.miniCart.salesorder.configs;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AmqpConfigs {

	@Bean
	public MessageConverter getConverter() {
		return new Jackson2JsonMessageConverter();
	}

}
