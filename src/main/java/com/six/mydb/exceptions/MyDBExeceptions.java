package com.six.mydb.exceptions;

public class MyDBExeceptions extends RuntimeException {

	private static final long serialVersionUID = 3849608996266385005L;

	public MyDBExeceptions() {
		super();
	}

	public MyDBExeceptions(String message, Throwable cause) {
		super(message, cause);
	}

	public MyDBExeceptions(String message) {
		super(message);
	}

	public MyDBExeceptions(Throwable cause) {
		super(cause);
	}

}
