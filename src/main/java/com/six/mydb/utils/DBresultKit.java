package com.six.mydb.utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

public class DBresultKit {

	// rs 转为 listmap
	public static List<Map<String, Object>> getResultToListMap(ResultSet rs) throws SQLException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		ResultSetMetaData metaData = rs.getMetaData();
		int n = rs.getMetaData().getColumnCount();
		if (rs != null) {
			while (rs.next()) {
				Map<String, Object> hashMap = new HashMap<String, Object>();
				for (int i = 1; i <= n; i++) {
					hashMap.put(convertColumn(metaData.getColumnLabel(i).toLowerCase(Locale.ENGLISH)), rs.getObject(i));
				}
				list.add(hashMap);
			}
		}
		return list;
	}

	// rs转bean
	public static <T> List<T> getResultToListBean(ResultSet rs, Class<T> clazz) throws Exception {
		List<Map<String, Object>> resultToListMap = getResultToListMap(rs);
		List<T> list = new ArrayList<>();
		for (Map<String, Object> map : resultToListMap) {
			T newInstance = clazz.newInstance();
			BeanUtils.populate(newInstance, map);
			list.add(newInstance);
		}
		return list;
	}

	/**
	 * 列转换
	 * 
	 * @param column
	 * @return
	 * @throws SQLException
	 */
	public static String convertColumn(String column) {
		String lowerCase = column;
		return lowerCase.contains("_") ? underlineToCamel(lowerCase) : lowerCase;
	}

	/**
	 * 字符串下划线转为驼峰
	 * 
	 * @param param
	 * @return
	 */
	public static String underlineToCamel(String param) {
		char ch = '_';
		if (param == null || "".equals(param.trim())) {
			return "";
		}
		int len = param.length();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = param.charAt(i);
			if (c == ch) {
				if (++i < len) {
					sb.append(Character.toUpperCase(param.charAt(i)));
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}
}
