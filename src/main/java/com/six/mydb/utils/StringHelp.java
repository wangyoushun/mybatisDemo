package com.six.mydb.utils;

public class StringHelp {

	public static String getBeanField(String str){
		return str.substring(3, 4).toLowerCase()+str.substring(4);
	}
}
