package com.six.mydb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.six.mydb.exceptions.MyDBExeceptions;
import com.six.mydb.utils.BeanKit;
import com.six.mydb.utils.DBresultKit;
import com.six.mydb.utils.FreeMarkerKit;
import com.six.mydb.utils.SqlKit;
import com.six.mydb.utils.StringHelp;

public class SqlSession {

	private Logger logger = Logger.getLogger(getClass());
	private Connection connection;
	private Config config;

	public SqlSession(Config config, Connection conn) {
		this.config = config;
		this.connection = conn;
	}

	// select
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
		PreparedStatement prepareStatement = null;
		ResultSet rs = null;
		List<T> resultList = null;

		try {
			prepareStatement = connection.prepareStatement(sql);
			rs = prepareStatement.executeQuery();
			String resultType = mapStatement.getResultType();
			if (resultType == null || resultType.length() == 0) {
				resultList = (List<T>) DBresultKit.getResultToListMap(rs);
			} else {
				resultList = (List<T>) DBresultKit.getResultToListBean(rs,
						Class.forName(resultType));
			}
		} finally {
			close(prepareStatement, rs);
		}

		return resultList;
	}

	// close
	private void close(PreparedStatement prepareStatement, ResultSet rs) {
		closeResultSet(rs);
		closeStatement(prepareStatement);
//		close();
	}

	public void closeStatement(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException ex) {
				logger.debug("Could not close JDBC Statement", ex);
			} catch (Throwable ex) {
				logger.debug("Unexpected exception on closing JDBC Statement",
						ex);
			}
		}
	}

	public void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException ex) {
				logger.trace("Could not close JDBC ResultSet", ex);
			} catch (Throwable ex) {
				logger.trace("Unexpected exception on closing JDBC ResultSet",
						ex);
			}
		}
	}

	// 拼接sql
	private String getSql(String sqlID, Object param, SqlConfig mapStatement)
			throws Exception {
		String sql = mapStatement.getSql();
		Map<String, Object> wrapCollection = wrapCollection(param);
		sql = builderSql(sql, wrapCollection);
		logger.debug("sql: -- " + sql);
		return sql;
	}

	// freemarker 构建动态sql
	private String builderSql(String sql, Map<String, Object> wrapCollection) {
		return FreeMarkerKit.parseSql(sql, wrapCollection);
	}

	// 传参封装
	@SuppressWarnings("unchecked")
	private Map<String, Object> wrapCollection(Object param) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		if (param == null) {

		} else {
			if (param instanceof Map) {
				map = (Map<String, Object>) param;
			} else {
				Class<? extends Object> clazz = param.getClass();
				if (TypeConstants.contains(clazz)) {
					map.put("_param", param);
				} else {
					map = BeanKit.transBean2Map(param);
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

	public int insert(String sqlID) {
		return insert(sqlID, null);
	}

	public int insert(String sqlID, Object param) {
		return update(sqlID, param);
	}

	public int update(String sqlID) {
		return update(sqlID, null);
	}

	public int update(String sqlID, Object param) {
		int executeUpdate = 0;
		try {
			SqlConfig mapStatement = config.getSqlMap().get(sqlID);
			if (mapStatement == null)
				throw new MyDBExeceptions("no sqlid " + sqlID + " in sql");

			String type = mapStatement.getType().trim();
			if (!(type.equals("insert")
					|| type.equals("update") || type.equals("delete"))) {
				throw new MyDBExeceptions("sqlid " + sqlID
						+ " not insert/update/detele");
			}

			// 组装sql
			String sql = getSql(sqlID, param, mapStatement);
			PreparedStatement prepareStatement = null;
			try {
				prepareStatement = connection.prepareStatement(sql);
				executeUpdate = prepareStatement.executeUpdate();
			} finally {
				close(prepareStatement, null);
			}

		} catch (Exception e) {
			throw new MyDBExeceptions(e);
		}
		return executeUpdate;
	}

	public int delete(String sqlID) {
		return delete(sqlID, null);
	}

	public int delete(String sqlID, Object param) {
		return update(sqlID, param);
	}

	public <T> int insert(T obj) {
		return executeSql(SqlKit.buildInsertSql(obj));
	}

	public <T> int updateById(T obj) {
		return executeSql(SqlKit.buildUpdateSql(obj));
	}

	public <T> int deleteById(T obj) {
		return executeSql(SqlKit.buildDeleteSql(obj));
	}

	// 原生态sql处理
	public int executeSql(String sql) {
		try {
			PreparedStatement prepareStatement = connection
					.prepareStatement(sql);
			return prepareStatement.executeUpdate();
		} catch (SQLException e) {
			throw new MyDBExeceptions(e.getMessage());
		}
	}

	public <T> int insertBatch(String sqlID, List<T> list, int batchSize) {
		int count = 0;

		try {
			SqlConfig mapStatement = config.getSqlMap().get(sqlID);
			if (mapStatement == null)
				throw new MyDBExeceptions("no sqlid " + sqlID + " in sql");

			String type = mapStatement.getType().trim();
			if (!(type.equals("insert")
					|| type.equals("update") || type.equals("delete"))) {
				throw new MyDBExeceptions("sqlid " + sqlID
						+ " not insert/update/detele");
			}

			int size = list.size();
			if (list == null || size == 0) {
				return 0;
			}

			PreparedStatement prepareStatement=null;
			try {
				String sql = mapStatement.getSql();
				 prepareStatement = connection
						.prepareStatement(sql);
				connection.setAutoCommit(false); // 开始事务

				for (int i = 0; i < size; i++) {
					T t = list.get(i);
					Map<String, Object> wrapCollection = wrapCollection(t);
					String builderSql = builderSql(sql, wrapCollection);
					prepareStatement.addBatch(builderSql);

					if ((i % batchSize == 0 && i != 0) || i == (size - 1)) {
						prepareStatement.executeBatch();
						commit();
						connection.setAutoCommit(false); // 开始事务
						System.out.println("------------>" + batchSize);

						if (i == size - 1) {
							count = size;
						} else {
							count += batchSize;
						}
					}
				}
			} finally {
				close(prepareStatement, null);
			}
		} catch (Exception e) {
			throw new MyDBExeceptions(e);
		}
		return count;
	}

	public <T> int deleteBatchByIds(List<T> list, int batchSize) {
		int count = 0;

		try {
			int size = list.size();
			if (list == null || size == 0) {
				return 0;
			}

			PreparedStatement prepareStatement=null;
			try {
				
				 prepareStatement = connection
						.prepareStatement("");
				connection.setAutoCommit(false); // 开始事务
				
				for (int i = 0; i < size; i++) {
					T t = list.get(i);
					String sql = SqlKit.buildDeleteSql(t);
					prepareStatement.addBatch(sql);

					if ((i % batchSize == 0 && i != 0) || i == (size - 1)) {
						prepareStatement.executeBatch();
						commit();
						connection.setAutoCommit(false); // 开始事务
						System.out.println("------------>" + batchSize);

						if (i == size - 1) {
							count = size;
						} else {
							count += batchSize;
						}
					}
				}
			} finally {
				close(prepareStatement, null);
			}
		} catch (Exception e) {
			throw new MyDBExeceptions(e);
		}
		return count;
	}

	
	
	// 开启事务
	public void start() throws SQLException {
		connection.setAutoCommit(false);
	}

	public void commit() throws SQLException {
		connection.commit();
	}

	public void rollback() {
		try {
			connection.rollback();
		} catch (Exception e) {
			throw new MyDBExeceptions();
		}
	}

	public void close() {
		try {
			connection.close();
		} catch (Exception e) {
			throw new MyDBExeceptions();
		}
	}
}
