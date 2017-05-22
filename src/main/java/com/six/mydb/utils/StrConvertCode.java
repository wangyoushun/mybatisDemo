package com.six.mydb.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;

//使用commons的jexl可实现将字符串变成可执行代码的功能 
public class StrConvertCode {
	
	/**
	 * 使用commons的jexl可实现将字符串变成可执行代码的功能 
	 */
	public static Object convertToCode(String jexlExp, Map<String, Object> map) {
		JexlEngine jexl = new JexlEngine();
		Expression e = jexl.createExpression(jexlExp);
		JexlContext jc = new MapContext();
		for (String key : map.keySet()) {
			jc.set(key, map.get(key));
		}
		if (null == e.evaluate(jc)) {
			return "";
		}
		return e.evaluate(jc);
	}
	
	public static void main(String[] args) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("money", 21000);
			String expression = "money>=2000&&money<=4000";
			Object code = convertToCode(expression, map);
			System.out.println(code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
