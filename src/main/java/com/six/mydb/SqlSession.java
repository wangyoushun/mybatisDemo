package com.six.mydb;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.six.mydb.exceptions.MyDBExeceptions;
import com.six.mydb.utils.DBresultKit;
import com.six.mydb.utils.FreeMarkerKit;
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
	public <T> List<T> selectList(String sqlID, Object param) throws Exception {

		SqlConfig mapStatement = config.getSqlMap().get(sqlID);
		if (mapStatement == null)
			throw new MyDBExeceptions("no sqlid " + sqlID + " in sql");

		if (!mapStatement.getType().equals("select")) {
			throw new MyDBExeceptions("sqlid " + sqlID + " not query");
		}

		// 组装sql
		String sql = getSql(sqlID, param, mapStatement);
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		ResultSet rs = prepareStatement.executeQuery();

		String resultType = mapStatement.getResultType();
		if (resultType == null || resultType.length() == 0) {
			return (List<T>) DBresultKit.getResultToListMap(rs);
		} else {
			return (List<T>) DBresultKit.getResultToListBean(rs,
					Class.forName(resultType));
		}
	}

	private String getSql(String sqlID, Object param, SqlConfig mapStatement)
			throws SQLException, ClassNotFoundException,
			IllegalAccessException, InvocationTargetException {
		String sql = mapStatement.getSql();
		Map<String, Object> wrapCollection = wrapCollection(sqlID, param,
				mapStatement);
		sql = builderSql(sql, wrapCollection);
		logger.debug("sql: -- " + sql);
		return sql;
	}

	private String builderSql(String sql, Map<String, Object> wrapCollection) {
		return FreeMarkerKit.parseSql(sql, wrapCollection);
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> wrapCollection(String sqlID, Object param,
			SqlConfig sqlConfig) throws SQLException, ClassNotFoundException,
			IllegalAccessException, InvocationTargetException {
		Map<String, Object> map = new HashMap<String, Object>();
		if (param == null) {

		} else {
			if (param instanceof Map) {
				map = (Map<String, Object>) param;
			} else {
				Class<?> clazz = param.getClass();
				Method[] methods = clazz.getMethods();
				for (Method method : methods) {
					String name = method.getName();
					if (name != null && name.length() > 3
							&& "get".equals(name.substring(0, 3))
							&& !"getClass".equals(name)) {
						String beanField = StringHelp.getBeanField(name);
						map.put(beanField, method.invoke(param));
					}
				}
			}

		}
		return convertSql(map);
	}

	// 防止sql注入 对特殊字符进行处理
	private HashMap<String, Object> convertSql(Map<String, Object> map) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>(
				map.size());
		Set<String> keySet = map.keySet();
		for (String key : keySet) {
			Object object = map.get(key);
			if (object instanceof String) {
				resultMap.put(key,
						StringHelp.transactSQLInjection((String) object));
			} else {
				resultMap.put(key, object);
			}
		}
		return resultMap;
	}

	public <T> List<T> selectList(String sqlID) throws Exception {
		return selectList(sqlID, null);
	}

	public <T> T selectOne(String sqlID) throws Exception {
		return selectOne(sqlID, null);
	}

	public <T> T selectOne(String sqlID, Object param) throws Exception {
		List<T> list = this.selectList(sqlID, param);
		if (list.size() == 1) {
			return list.get(0);
		} else if (list.size() > 1) {
			throw new MyDBExeceptions(
					"Expected one result (or null) to be returned by selectOne(), but found: "
							+ list.size());
		} else {
			return null;
		}
	}

	public int insert(String sqlID) throws Exception {
		return insert(sqlID, null);
	}

	public int insert(String sqlID, Object param) throws Exception {
		return update(sqlID, param);
	}

	public int uodate(String sqlID) throws Exception {
		return update(sqlID, null);
	}

	public int update(String sqlID, Object param) throws Exception {
		SqlConfig mapStatement = config.getSqlMap().get(sqlID);
		if (mapStatement == null)
			throw new MyDBExeceptions("no sqlid " + sqlID + " in sql");
		
		if (!(mapStatement.getType().equals("insert") || mapStatement.getType().equals("update"))) {
			throw new MyDBExeceptions("sqlid " + sqlID + " not insert/update");
		}

		// 组装sql
		String sql = getSql(sqlID, param, mapStatement);
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		return prepareStatement.executeUpdate();
	}

	public void close() throws SQLException {
		connection.close();
	}
}
