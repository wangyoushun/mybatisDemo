package com.six.demo;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestClass {

	@Test
	public void test01() throws Exception {
		String str="file:///D:/code/eclipse_bailian3/mybatisDemo/mydb-config.dtd";
		String substring = str.substring(str.lastIndexOf("/")+1);
		System.out.println(substring);
	}
	
	
	@Test
	public void testName() throws Exception {
		String pre = "${";
		String end = "}";

		String str = "${username}";
		int startIndex = str.indexOf(pre);
		int endIndex = str.indexOf(end);
		String substring = str.substring(startIndex + pre.length(), endIndex);
		// String replaceAll = str.replaceAll("${", "");
		// System.out.println(replaceAll);
		System.out.println(substring);
	}
}
