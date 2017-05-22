package com.six.mydb;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.six.mydb.exceptions.MydbExeceptions;
import com.six.mydb.utils.StringHelp;

public class SqlSession {

	private Logger logger = Logger.getLogger(getClass());
	private Connection connection;
	private Config config;

	public SqlSession(Config config, Connection conn) {
		this.config = config;
		this.connection = conn;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> selectList(String sqlId, Object param) throws Exception {

		MapStatement mapStatement = config.getSqlMap().get(sqlId);
		if (mapStatement == null)
			throw new MydbExeceptions("no sqlid " + sqlId + " in xml");

		//组装sql
		
		String sql = mapStatement.getSqlStr();
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		logger.debug("sql: -- " + sql);

		if (param == null) {

		} else {
			if (param instanceof List) {
				// map.put("list", param);
			} else if (param.getClass().isArray()) {
				// map.put("array", param);
			} else {
				String parameterType = mapStatement.getParameterType();
				if (parameterType == null) {
					prepareStatement.setObject(1, param);
				} else if ("java.util.Map".equals(parameterType)) {
					List<String> paramKey = mapStatement.getParamKey();
					Map<String, Object> map = (Map<String, Object>) param;
					for (int i = 0; i < paramKey.size(); i++) {
						Object object = map.get(paramKey.get(i));
						if (object == null)
							throw new MydbExeceptions(sqlId + " no param "
									+ paramKey.get(i));

						prepareStatement.setObject(i + 1, object);
					}
				} else {
					Class<?> clazz = Class.forName(parameterType);
					Method[] methods = clazz.getMethods();
					Map<String, Method> map = new HashMap<String, Method>();
					for (Method method : methods) {
						String name = method.getName();
						if (name != null && name.length() > 3
								&& "get".equals(name.substring(0, 3))
								&& !"getClass".equals(name)) {
							String beanField = StringHelp.getBeanField(name);
							map.put(beanField, method);
						}
					}

					List<String> paramKey = mapStatement.getParamKey();
					Method _method = null;
					for (int i = 0; i < paramKey.size(); i++) {
						_method = map.get(paramKey.get(i));
						if (_method == null)
							throw new MydbExeceptions(sqlId + " no param "
									+ paramKey.get(i));

						Object invoke = _method.invoke(param);
						prepareStatement.setObject(i + 1, invoke);
					}

				}
			}
		}

		ResultSet rs = prepareStatement.executeQuery();
		
		String resultType = mapStatement.getResultType();
		if(resultType==null || resultType.length()==0){
			return (List<T>)resultToListMap(rs);
		}else{
			return (List<T>)resultToListObj(rs, Class.forName(resultType));
		}
	}

	/**
	 * 结果集转为对象
	 */
	private  <T> List<T> resultToListObj(ResultSet rs, Class<T> clazz)
			throws SQLException, Exception {
		List<T> list = new ArrayList<T>();
		if (rs != null) {
			T obj = clazz.newInstance();
			while (rs.next()) {
				ResultSetMetaData metaData = rs.getMetaData();
				for (int i = 1; i <= metaData.getColumnCount(); i++) {
					String key = underlineToCamel(metaData.getColumnLabel(i)
							.toLowerCase());
					Method[] methods = clazz.getMethods();
					for (Method method : methods) {
						String name = method.getName();
						if (name != null && name.length() > 3
								&& "set".equals(name.substring(0, 3))) {
							name = name.substring(3);
							if (name.equals(key.substring(0, 1).toUpperCase()
									+ key.substring(1))) {
								Class<?>[] parameterTypes = method
										.getParameterTypes();
								if (parameterTypes[0] == Integer.class) {
									method.invoke(obj, rs.getInt(i));
								} else if (parameterTypes[0] == String.class) {
									method.invoke(obj, rs.getString(i));
								} else if (parameterTypes[0] == Long.class) {
									method.invoke(obj, rs.getLong(i));
								} else if (parameterTypes[0] == Double.class) {
									method.invoke(obj, rs.getDate(i));
								} else if (parameterTypes[0] == Byte.class) {
									method.invoke(obj, rs.getByte(i));
								} else if (parameterTypes[0] == Float.class) {
									method.invoke(obj, rs.getFloat(i));
								} else if (parameterTypes[0] == Date.class) {
									method.invoke(obj, rs.getDate(i));
								}
							}
						}
					}
				}
				list.add(obj);
			}
		}
		return list;
	}

	/**
	 * rs结果集转化为map
	 */
	private List<Map<String, Object>> resultToListMap(ResultSet rs)
			throws SQLException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		if (rs != null) {
			while (rs.next()) {
				ResultSetMetaData metaData = rs.getMetaData();
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 1; i < metaData.getColumnCount(); i++) {
					map.put(metaData.getColumnLabel(i), rs.getObject(i));
				}
				list.add(map);
			}
		}
		return list;
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

	public void close() throws SQLException {
		connection.close();
	}
}
