package com.miniCart.salesorder.models;

public class StatusResponse {

	private String message;
	private String environment;

	public StatusResponse() {
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

}
