package com.six.mydb;

import java.io.IOException;
import java.io.InputStream;

public class Resources {

	private static ClassLoader systemClassLoader = ClassLoader
			.getSystemClassLoader();

	public static InputStream getResourceAsStream(String resources)
			throws IOException {
		InputStream in = systemClassLoader.getResourceAsStream(resources);
		if (in == null) {
			throw new IOException("Could not find resource  " + resources);
		}
		return in;
	}
}
