package com.miniCart.salesorder.models;

import java.util.List;

public class OrderDto {

	private long customerId;
	private String orderDescription;
	private List<ItemNameAndCount> itemNamesAndCount;

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

	public List<ItemNameAndCount> getItemNamesAndCount() {
		return itemNamesAndCount;
	}

	public void setItemNamesAndCount(List<ItemNameAndCount> itemNamesAndCount) {
		this.itemNamesAndCount = itemNamesAndCount;
	}

}
