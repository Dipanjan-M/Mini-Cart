package com.miniCart.salesorder.models;

import java.util.Map;

public class OrderDto {

	private long customerId;
	private String orderDescription;
	private Map<String, Integer> itemNamesAndCount;

	public OrderDto() {
		super();
	}

	public String getOrderDescription() {
		return orderDescription;
	}

	public void setOrderDescription(String orderDescription) {
		this.orderDescription = orderDescription;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public Map<String, Integer> getItemNamesAndCount() {
		return itemNamesAndCount;
	}

	public void setItemNamesAndCount(Map<String, Integer> itemNamesAndCount) {
		this.itemNamesAndCount = itemNamesAndCount;
	}

}
