package com.six.mydb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.six.mydb.exceptions.MydbExeceptions;

public class SqlSession {

	private Connection connection;
	private Config config;

	public SqlSession(Config config, Connection conn) {
		this.config = config;
		this.connection = conn;
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> selectList(String sqlId, Object param) throws SQLException {
		
		MapStatement mapStatement = config.getSqlMap().get(sqlId);
		String sql = mapStatement.getSqlStr();
		if(sql==null || "".equals(sql))
			throw new MydbExeceptions("no sqlid "+sqlId +" in xml");
		
		System.out.println(mapStatement);
		
		
		
		//
//		TokenParser tokenParser = new TokenParser();
//		String parserSql = tokenParser.parserSql(sql);
//		PreparedStatement prepareStatement = connection.prepareStatement(parserSql);
//		prepareStatement.setObject(parameterIndex, x);
		
		
		
		List<Map<String, Object>> rsList = null;
		
//
//		
//		
//		
//		
//		ResultSet rs = prepareStatement.executeQuery();
//
//		rsList = resultToListMap(rs);

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

	public void close() throws SQLException{
		connection.close();
	}
}
