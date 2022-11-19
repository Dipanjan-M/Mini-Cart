package com.miniCart.customer.configs;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.miniCart.customer.constants.AMQPConstants;

@Configuration
public class RabbitMQConfig {

	@Bean
	public Queue queue() {
		return new Queue(AMQPConstants.QUEUE_NAME);
	}

	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(AMQPConstants.EXCHANGE_NAME);
	}

	@Bean
	public Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(AMQPConstants.ROUTING_KEY);
	}

	@Bean
	public MessageConverter msgConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean("myAmqpTemplateBean")
	public AmqpTemplate getTemplate(ConnectionFactory connectionFactory) {
		final RabbitTemplate template = new RabbitTemplate(connectionFactory);
		template.setMessageConverter(msgConverter());
		return template;
	}

}
