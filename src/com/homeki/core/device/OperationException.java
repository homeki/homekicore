package com.homeki.core.device;

public class OperationException extends RuntimeException {
	private static final long serialVersionUID = -8629338813839338899L;

	private String message;
	
	public OperationException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
}
