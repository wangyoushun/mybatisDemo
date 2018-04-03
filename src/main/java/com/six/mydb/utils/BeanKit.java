package com.six.mydb.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.six.domain.User;

/**
 * bean -- map 转换工具类
 */
public class BeanKit {
	// Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map
	public static Map<String, Object> transBean2Map(Object obj) {

		if (obj == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();

				// 过滤class属性
				if (!key.equals("class")) {
					// 得到property对应的getter方法
					Method getter = property.getReadMethod();
					Object value = getter.invoke(obj);

					map.put(key, value);
				}

			}
		} catch (Exception e) {
			System.out.println("transBean2Map Error " + e);
		}

		return map;

	}
	
	@Test
	public void testName() throws Exception {
		User user = new User();
		user.setAddress("123");
		user.setAge(21);
		user.setName("sdf");
		List<String> asList = Arrays.asList(new String[]{"1","2"});
		user.setList(asList);
		
		User user2 = new User();
		user2.setName("user2");
		user2.setAge(123);
		user.setU(user2);
		
		
		Map<String, Object> transBean2Map = transBean2Map(user);
		System.out.println(transBean2Map);
		User object = (User) transBean2Map.get("u");
		System.out.println(object.getName());
	}
}
