package com.six.mydb.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.six.mydb.exceptions.MyDBExeceptions;

/**
  sql生成工具类
 */
public class SqlKit {
	//面向对象生成delete语句
	public static <T> String buildDeleteSql(T t) {
		if (t == null) {
			return "";
		}
		String tableName = StringTool.camelTounderline(t.getClass().getSimpleName());
		Map<String, Object> transBean2Map = BeanKit.transBean2Map(t);
		if(!transBean2Map.containsKey("id")){
			throw new MyDBExeceptions("obj no id");
		}
		
		String sql = "delete from " + tableName.toLowerCase() + " where id=" + transBean2Map.get("id");
		LogKit.debug(sql);
		return sql;
	}

	public static <T> String buildUpdateSql(T t) {
		Map<String, Object> transBean2Map = BeanKit.transBean2Map(t);
		Object id = transBean2Map.get("id");
		
		if(!transBean2Map.containsKey("id") || id==null){
			throw new MyDBExeceptions("obj no id");
		}
		
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		Set<String> keySet = transBean2Map.keySet();
		for (String key : keySet) {
			Object object = transBean2Map.get(key);
			if (object != null && !"id".equals(key)) {
				hashMap.put(key, object);
			}
		}
		
		keySet = hashMap.keySet();
		String tableName = StringTool.camelTounderline(t.getClass().getSimpleName());

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("update ").append(tableName.toLowerCase()).append(" set ");
		for (String key : keySet) {
			Object object = hashMap.get(key);
			stringBuilder.append(key).append("='").append(object).append("', ");
		}

		String string2 = stringBuilder.toString();
		String substring = string2.substring(0, string2.lastIndexOf(","));
		substring += " where id= " + id;
		LogKit.debug(substring);
		
		return substring;
	}

	public static <T> String buildInsertSql(T t) {
		Map<String, Object> transBean2Map = BeanKit.transBean2Map(t);
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		Set<String> keySet = transBean2Map.keySet();
		for (String key : keySet) {
			Object object = transBean2Map.get(key);
			if (object != null) {
				hashMap.put(key, object);
			}
		}

		keySet = hashMap.keySet();
		String tableName = StringTool.camelTounderline(t.getClass().getSimpleName());

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("insert into ").append(tableName.toLowerCase()).append(" (");
		for (String key : keySet) {
			stringBuilder.append(key).append(",");
		}

		String string = stringBuilder.toString();
		String substring = string.substring(0, string.lastIndexOf(","));
		StringBuilder stringBuilder2 = new StringBuilder(substring);
		stringBuilder2.append(") values (");
		for (String key : keySet) {
			Object object = hashMap.get(key);
			stringBuilder2.append("'").append(object).append("',");
		}

		string = stringBuilder2.toString();
		substring = string.substring(0, string.lastIndexOf(","))+" )";
		LogKit.debug(substring);
		return substring;
	}
}
