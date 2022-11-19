package com.miniCart.salesorder.entities;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "sales_order")
public class SalesOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private long id;
	private LocalDate orderDate;
	private String orderDesc;
	private double totalPrice;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "cust_id", referencedColumnName = "custId", nullable = false)
	private CustomerSoS customerSoS;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "salesOrder", targetEntity = OrderLineItem.class, cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<OrderLineItem> items;

	public SalesOrder() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDate getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderDesc() {
		return orderDesc;
	}

	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public CustomerSoS getCustomerSoS() {
		return customerSoS;
	}

	public void setCustomerSoS(CustomerSoS customerSoS) {
		this.customerSoS = customerSoS;
	}

	public List<OrderLineItem> getItems() {
		return items;
	}

	public void setItems(List<OrderLineItem> items) {
		this.items = items;
	}

}
