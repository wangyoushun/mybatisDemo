package com.six.mydb.utils;

import java.util.HashMap;
import java.util.Map;

import ognl.Ognl;
import ognl.OgnlException;

import org.junit.Test;
import org.w3c.dom.Node;

import com.six.mydb.entity.User;


public class OgnlHelper {

	public static Object getValue(String expression, Object root)
			throws OgnlException {
		Object node = Ognl.parseExpression(expression);
		Object value = Ognl.getValue(node, root);
		return value;
	}
	
	@Test
	public void testName() throws Exception {
		User user = new User();
		user.setId(123);
		user.setName("234");
		String name="345";
		Object node = Ognl.parseExpression("#{name}");
		System.out.println(node);
//		Object value = Ognl.getValue(node);
//		System.out.println(value);
	}
}


