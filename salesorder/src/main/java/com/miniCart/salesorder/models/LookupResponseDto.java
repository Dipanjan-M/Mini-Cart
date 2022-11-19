package com.miniCart.salesorder.models;

import java.time.LocalDate;

import com.miniCart.salesorder.entities.SalesOrder;

public class LookupResponseDto {

	private long orderId;
	private String orderDesc;
	private LocalDate orderDate;
	private double orderAmt;

	public LookupResponseDto() {
		super();
	}

	public LookupResponseDto(SalesOrder order) {
		this.orderId = order.getId();
		this.orderDesc = order.getOrderDesc();
		this.orderDate = order.getOrderDate();
		this.orderAmt = order.getTotalPrice();
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getOrderDesc() {
		return orderDesc;
	}

	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public double getOrderAmt() {
		return orderAmt;
	}

	public void setOrderAmt(double orderAmt) {
		this.orderAmt = orderAmt;
	}

}
