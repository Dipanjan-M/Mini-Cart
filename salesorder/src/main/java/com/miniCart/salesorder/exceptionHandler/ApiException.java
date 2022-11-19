package com.miniCart.salesorder.exceptionHandler;

@SuppressWarnings("serial")
public class ApiException extends RuntimeException {

	public ApiException(String msg) {
		super(msg);
	}

}
