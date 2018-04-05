package com.six.mydb.session;

import java.util.List;

import com.six.mydb.Page;

public interface SqlSession {
	public <T> T selectOne(String sqlID);
	
	public <T> T selectOne(String sqlID, Object param);
	
	public <T> List<T> selectList(String sqlID);
	
	public <T> List<T> selectList(String sqlID, Object param);
	
	public <T> List<T> selectListPage(String sqlID, Object param, Page page);
	
	public int insert(String sqlID);
	
	public int insert(String sqlID, Object param);
	
	public <T> int insert(T obj);
	
	public int update(String sqlID) ;
	
	public <T> int updateById(T obj) ;
	
	public int update(String sqlID, Object param);
	
	public int delete(String sqlID) ;
	
	public int delete(String sqlID, Object param);
	
	public <T> int deleteById(T obj);
	
	public int executeSql(String sql) ;
	
	public <T> int insertBatch(String sqlID, List<T> list, int batchSize);
	
	public <T> int deleteBatchByIds(List<T> list, int batchSize);
	
}
