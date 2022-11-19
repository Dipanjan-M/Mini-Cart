package com.miniCart.item.models;

import org.springframework.validation.FieldError;

public class CustomValidationError {

	private String field;
	private String errorMsg;

	public CustomValidationError(FieldError err) {
		super();
		this.field = err.getField();
		this.errorMsg = err.getDefaultMessage();
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
