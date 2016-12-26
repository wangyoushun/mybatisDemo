package com.six.mydb;

import java.util.Properties;

public class TokenParser {

	private Properties properties;
	private String pre = "${";
	private String end = "}";
	private Config config;

	public TokenParser(Properties properties) {
		this.properties = properties;
	}

	public TokenParser(Config config) {
		this.config = config;
	}

	public TokenParser() {
	}

	public String parser(String str) {
		int startIndex = str.indexOf(pre);
		int endIndex = str.indexOf(end);
		str = str.substring(startIndex + pre.length(), endIndex);

		if (properties != null && properties.containsKey(str)) {
			return properties.getProperty(str);
		}
		return str;
	}

	public String parserSql(String str) {
		while (str.contains(pre) && str.contains(end)) {
			int startIndex = str.indexOf(pre);
			int endIndex = str.indexOf(end);
			String subBefore = str.substring(0, startIndex);
			String subAfter = str.substring(endIndex + 1);
			str = subBefore + "?" + subAfter;
		}
		return str;
	}

	// public String parserSql(String str) {
	// int startIndex = str.indexOf(pre);
	// int endIndex = str.indexOf(end);
	// String subBefore = str.substring(0, startIndex);
	// String subAfter = str.substring(endIndex + 1);
	// String sql = str.substring(startIndex + pre.length(), endIndex);
	// if (properties != null && properties.containsKey(sql)) {
	// str = subBefore + properties.getProperty(sql) + subAfter;
	// }
	// return str;
	// }
}
