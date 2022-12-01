package com.miniCart.customer.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.validation.FieldError;

public class CustomValidationError {

	private Map<String, List<String>> validationErrResponses;

	public CustomValidationError() {
		super();
	}

	public CustomValidationError(List<FieldError> errs) {
		super();
		this.validationErrResponses = new HashMap<>();
		for (FieldError err : errs) {
			if (this.validationErrResponses.get(err.getField()) != null) {
				List<String> errMsgs = this.validationErrResponses.get(err.getField());
				errMsgs.add(err.getDefaultMessage());
				this.validationErrResponses.put(err.getField(), errMsgs);
			} else {
				List<String> msgContainer = new ArrayList<>();
				msgContainer.add(err.getDefaultMessage());
				this.validationErrResponses.put(err.getField(), msgContainer);
			}
		}
	}

	public Map<String, List<String>> getValidationErrResponses() {
		return validationErrResponses;
	}

	public void setValidationErrResponses(Map<String, List<String>> validationErrResponses) {
		this.validationErrResponses = validationErrResponses;
	}

}
