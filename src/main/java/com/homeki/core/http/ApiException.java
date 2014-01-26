package com.homeki.core.http;

public class ApiException extends RuntimeException {
	private static final long serialVersionUID = 1923564455163159133L;

	public ApiException(String message) {
		super(message);
	}
}
