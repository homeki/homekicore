package com.homeki.core.http;

public class MissingKeyException extends Exception {
	public MissingKeyException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 1L;
}
