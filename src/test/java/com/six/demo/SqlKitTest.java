package com.six.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.six.domain.User;
import com.six.mydb.utils.BeanKit;
import com.six.mydb.utils.StringTool;

public class SqlKitTest {
	@Test
	public void testName() throws Exception {
		// bean 转sql
		User user = new User();
		user.setAddress("12323");
		user.setId(2323);

		String buildInsertSql = buildInsertSql(user);
		System.out.println(buildInsertSql);

		String buildUpdateSql = buildUpdateSql(user);
		// update
		System.out.println(buildUpdateSql);
		
		String buildDeleteSql = buildDeleteSql(user);
		System.out.println(buildDeleteSql);

	}
	
	private String buildDeleteSql(User user) {
		if(user==null){
			return "";
		}
		String tableName = StringTool.camelTounderline(user.getClass().getSimpleName());

		String sql = "delete from "+tableName.toLowerCase()+" where id="+user.getId();
		return sql;
	}

	

	private String buildUpdateSql(User user) {
		Map<String, Object> transBean2Map = BeanKit.transBean2Map(user);
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		Set<String> keySet = transBean2Map.keySet();
		for (String key : keySet) {
			Object object = transBean2Map.get(key);
			if (object != null && !"id".equals(key)) {
				hashMap.put(key, object);
			}
		}

		keySet = hashMap.keySet();
		String tableName = StringTool.camelTounderline(user.getClass().getSimpleName());

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("update ").append(tableName.toLowerCase()).append(" set ");
		for (String key : keySet) {
			Object object = hashMap.get(key);
			stringBuilder.append(key).append("='").append(object).append("', ");
		}

		String string2 = stringBuilder.toString();
		String substring = string2.substring(0, string2.lastIndexOf(","));
		substring += " where id= "+user.getId();
		return substring;
	}
	
	
	private String buildInsertSql(User user) {
		Map<String, Object> transBean2Map = BeanKit.transBean2Map(user);
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		Set<String> keySet = transBean2Map.keySet();
		for (String key : keySet) {
			Object object = transBean2Map.get(key);
			if (object != null) {
				hashMap.put(key, object);
			}
		}

		keySet = hashMap.keySet();
		String tableName = StringTool.camelTounderline(user.getClass().getSimpleName());

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
		substring = string.substring(0, string.lastIndexOf(","));
		substring += ")";
		return substring;
	}
}
