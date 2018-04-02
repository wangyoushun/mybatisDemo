package com.six.mydb.utils;

import ognl.Ognl;
import ognl.OgnlException;

import org.junit.Test;

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
		user.setName("");
		String name="345";
		Object node = Ognl.parseExpression("#{name==''}");
		System.out.println(node);
		Object value = Ognl.getValue(node,user);
		System.out.println(value);
	}
}


