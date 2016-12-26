package com.six.mydb.exceptions;

public class MydbExeceptions extends RuntimeException {

	private static final long serialVersionUID = 3849608996266385005L;

	public MydbExeceptions() {
		super();
	}

	public MydbExeceptions(String message, Throwable cause) {
		super(message, cause);
	}

	public MydbExeceptions(String message) {
		super(message);
	}

	public MydbExeceptions(Throwable cause) {
		super(cause);
	}

}
