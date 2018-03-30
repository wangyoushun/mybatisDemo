package com.six.mydb;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.six.mydb.exceptions.MyDBExeceptions;
import com.six.mydb.utils.DBresultKit;
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

		MapStatement mapStatement = config.getSqlMap().get(sqlID);
		if (mapStatement == null)
			throw new MyDBExeceptions("no sqlid " + sqlID + " in xml");

		// 组装sql
		String sql = mapStatement.getSqlStr();
		PreparedStatement prepareStatement = connection.prepareStatement(sql);
		logger.debug("sql: -- " + sql);

		wrapCollection(sqlID, param, mapStatement, prepareStatement);

		ResultSet rs = prepareStatement.executeQuery();

		String resultType = mapStatement.getResultType();
		if (resultType == null || resultType.length() == 0) {
			return (List<T>) DBresultKit.getResultToListMap(rs);
		} else {
			return (List<T>) DBresultKit.getResultToListBean(rs, Class.forName(resultType));
		}
	}

	private void wrapCollection(String sqlID, Object param, MapStatement mapStatement,
			PreparedStatement prepareStatement)
			throws SQLException, ClassNotFoundException, IllegalAccessException, InvocationTargetException {
		if (param == null) {

		} else {
			
			
			
			
			
			
			
			setStatementParam(sqlID,param,mapStatement,prepareStatement);
			/*
			HashMap<String,Object> hashMap = new HashMap<String, Object>();
			if(param instanceof List){
				hashMap.put("list", param);
			}else if(param.getClass().isArray()){
				hashMap.put("array", param);
			}else if(param instanceof Map) {
				
			}else{
				String parameterType = mapStatement.getParameterType();
				Class<?> clazz = Class.forName(parameterType);
				Method[] methods = clazz.getMethods();
				Map<String, Method> map = new HashMap<String, Method>();
				for (Method method : methods) {
					String name = method.getName();
					if (name != null && name.length() > 3 && "get".equals(name.substring(0, 3))
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
						throw new MyDBExeceptions(sqlID + " no param " + paramKey.get(i));

					Object invoke = _method.invoke(param);
					prepareStatement.setObject(i + 1, invoke);
				}

				
			}
			
			//
			
			
		*/}
	}

	@SuppressWarnings("unchecked")
	private void setStatementParam(String sqlID, Object param, MapStatement mapStatement,
			PreparedStatement prepareStatement)
			throws SQLException, ClassNotFoundException, IllegalAccessException, InvocationTargetException {
		String parameterType = mapStatement.getParameterType();
		if (parameterType == null) {
			prepareStatement.setObject(1, param);
		} else if ("java.util.Map".equals(parameterType)) {
			List<String> paramKey = mapStatement.getParamKey();
			Map<String, Object> map = (Map<String, Object>) param;
			for (int i = 0; i < paramKey.size(); i++) {
				Object object = map.get(paramKey.get(i));
				if (object == null)
					throw new MyDBExeceptions(sqlID + " no param " + paramKey.get(i));

				prepareStatement.setObject(i + 1, object);
			}
		} else {
			Class<?> clazz = Class.forName(parameterType);
			Method[] methods = clazz.getMethods();
			Map<String, Method> map = new HashMap<String, Method>();
			for (Method method : methods) {
				String name = method.getName();
				if (name != null && name.length() > 3 && "get".equals(name.substring(0, 3))
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
					throw new MyDBExeceptions(sqlID + " no param " + paramKey.get(i));

				Object invoke = _method.invoke(param);
				prepareStatement.setObject(i + 1, invoke);
			}

		}
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
					"Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
		} else {
			return null;
		}
	}
	
//	public int insert(String sqlID, Object parameter){
//		
//		
//	}

	public void close() throws SQLException {
		connection.close();
	}
}
