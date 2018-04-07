package com.six.mydb.mapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import com.six.mydb.Page;
import com.six.mydb.config.Config;
import com.six.mydb.config.SqlConfig;
import com.six.mydb.session.SqlSession;
import com.six.mydb.utils.LogKit;

public class MapperProxy<T> implements InvocationHandler {

	private SqlSession sqlSession;
	private Config config;
	private Class<T> clazz;

	public MapperProxy(SqlSession sqlSession, Config config, Class<T> clazz) {
		this.sqlSession = sqlSession;
		this.config = config;
		this.clazz=clazz;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		LogKit.info("proxy ");
		
		String sqlID = clazz.getSimpleName() + "." + method.getName();
		System.out.println(sqlID);
		Map<String, SqlConfig> sqlMap = config.getSqlMap();
		SqlConfig sqlConfig = sqlMap.get(sqlID);
		String type = sqlConfig.getType();
		Object param = null;
		if (args != null && args.length > 0) {
			param = args[0];
		}
		Object reuslt=null;
		
		if ("insert".equals(type)) {
			reuslt = sqlSession.insert(sqlID, param);
		} else if ("update".equals(type)) {
			reuslt = sqlSession.update(sqlID, param);
		} else if ("delete".equals(type)) {
			reuslt = sqlSession.delete(sqlID, param);
		} else if ("select".equals(type)) {
			String substring = sqlID.substring(sqlID.length()-4);
			if(substring.toLowerCase().contains("page")){
				Page page = (Page) args[1];
				reuslt = sqlSession.selectListPage(sqlID, param, page);
			}else{
				reuslt = sqlSession.selectList(sqlID, param);
			}
		}

		return reuslt;
	}

}
