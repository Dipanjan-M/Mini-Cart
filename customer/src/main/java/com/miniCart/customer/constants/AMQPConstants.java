package com.miniCart.customer.constants;

public class AMQPConstants {
	
	public static final String QUEUE_NAME = "Customer_Created_Event";
	public static final String EXCHANGE_NAME = "custo_to_oas_queue_exchng";
	public static final String ROUTING_KEY = "salesOrder.service.customerCreated";

}
