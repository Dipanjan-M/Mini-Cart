package com.miniCart.salesorder.listeners;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.miniCart.salesorder.entities.CustomerSoS;
import com.miniCart.salesorder.models.CustomerCreatedEvent;
import com.miniCart.salesorder.services.SalesOrderService;

@Component
public class CustomerCreatedEventListener {

	@Autowired
	private SalesOrderService salesOrderService;

	@RabbitListener(queues = "Customer_Created_Event")
	public void consumeMessageFromQueue(CustomerCreatedEvent event) {
		CustomerSoS custSoS = new CustomerSoS(event);
		this.salesOrderService.saveCustomerSoSDataFromEvent(custSoS);
	}

}
