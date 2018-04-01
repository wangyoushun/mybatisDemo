package com.six.mydb.utils;

public class StringHelp {

	public static String getBeanField(String str) {
		return str.substring(3, 4).toLowerCase() + str.substring(4);
	}
	
	// 处理sql注入
	public static String transactSQLInjection(String str) {
		return str.replaceAll(".*([';]+|(--)+).*", " ");
	}

}
