package com.six.mydb.utils;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PropertiesUtil {
	private static final Log LOG = LogFactory.getLog(PropertiesUtil.class);
	private static Properties properties = new Properties();

	public static String readConfig(String key) {
		return (String) properties.get(key);
	}

	public static Properties newInstance() {
		return properties;
	}

	public static Properties newInstance(String path) {
		try {
			properties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream(path));
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
		return properties;
	}

	public static void main(String[] args) {
		String name = "jdbc.properties";
		Properties properties = new Properties();
		try {
			properties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream(name));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Set<Object> keySet = properties.keySet();
		for (Object object : keySet) {
			System.out.println(object);
		}
	}
}
