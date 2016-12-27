package com.six.mydb;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.six.mydb.exceptions.MydbExeceptions;

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
		List<Map<String, Object>> rsList = resultToListMap(rs);
		return (List<T>) rsList;
	}

	private static List<Map<String, Object>> resultToListMap(ResultSet rs)
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

	public void close() throws SQLException {
		connection.close();
	}
}
