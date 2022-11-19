package com.miniCart.salesorder.models;

import java.time.LocalDate;
import java.util.List;

import com.miniCart.salesorder.entities.OrderLineItem;
import com.miniCart.salesorder.entities.SalesOrder;

public class OrderResponseDto {

	private long orderId;
	private String orderDescription;
	private LocalDate orderDate;
	private List<OrderLineItem> orderedItems;

	public OrderResponseDto() {
		super();
	}

	public OrderResponseDto(SalesOrder order) {
		this.orderId = order.getId();
		this.orderDescription = order.getOrderDesc();
		this.orderDate = order.getOrderDate();
		this.orderedItems = order.getItems();
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getOrderDescription() {
		return orderDescription;
	}

	public void setOrderDescription(String orderDescription) {
		this.orderDescription = orderDescription;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public List<OrderLineItem> getOrderedItems() {
		return orderedItems;
	}

	public void setOrderedItems(List<OrderLineItem> orderedItems) {
		this.orderedItems = orderedItems;
	}

}
