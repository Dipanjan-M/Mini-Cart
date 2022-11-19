package com.miniCart.salesorder.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.miniCart.salesorder.models.CustomerCreatedEvent;

@Entity
@Table(name = "customer_sos")
public class CustomerSoS {

	@Id
	private long custId;
	private String custFirstName;
	private String custLastName;
	private String custEmail;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "customerSoS", targetEntity = SalesOrder.class, cascade = CascadeType.ALL)
	@JsonIgnore
	private List<SalesOrder> orders;

	public CustomerSoS() {
		super();
	}

	public CustomerSoS(CustomerCreatedEvent event) {
		this.custId = event.getId();
		this.custFirstName = event.getFirstName();
		this.custLastName = event.getLastName();
		this.custEmail = event.getEmail();
	}

	public long getCustId() {
		return custId;
	}

	public void setCustId(long custId) {
		this.custId = custId;
	}

	public String getCustFirstName() {
		return custFirstName;
	}

	public void setCustFirstName(String custFirstName) {
		this.custFirstName = custFirstName;
	}

	public String getCustLastName() {
		return custLastName;
	}

	public void setCustLastName(String custLastName) {
		this.custLastName = custLastName;
	}

	public String getCustEmail() {
		return custEmail;
	}

	public void setCustEmail(String custEmail) {
		this.custEmail = custEmail;
	}

	public List<SalesOrder> getOrders() {
		return orders;
	}

	public void setOrders(List<SalesOrder> orders) {
		this.orders = orders;
	}

}
