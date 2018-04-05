package com.six.mydb;

import java.util.ArrayList;
import java.util.List;

public class TypeConstants {
	private static List<Class<?>> list = new ArrayList<Class<?>>();

	static {
		list.add(int.class);
		list.add(Integer.class);
		list.add(float.class);
		list.add(Float.class);
		list.add(double.class);
		list.add(Double.class);
		list.add(long.class);
		list.add(Long.class);
		list.add(String.class);
	}

	public static boolean contains(Class<?> clazz) {
		return list.contains(clazz);
	}
}
