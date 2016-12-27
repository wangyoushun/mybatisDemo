package com.six.mydb;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.six.mydb.entity.User;



public class Main {

	public static void main(String[] args) throws Exception {
//		test011();
		
		String configPath="mydb-config.xml";
		SqlSession session = new SqlSessionFactory(configPath).opsession();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", 2308);
		map.put("name", "叶小民1");
		List<Object> selectList = session.selectList("selectUserByMap", map);
		System.out.println(selectList);
		session.close();
	
		
	}

	private static void test011() throws Exception, SQLException {
		String configPath="mydb-config.xml";
		SqlSession session = new SqlSessionFactory(configPath).opsession();
		Integer id=2308;
//		List<Object> selectList = session.selectList("selectAllUser", id);
		User user = new User();
		user.setName("叶小民1");
		user.setId(id);
		List<Object> selectList = session.selectList("selectUserByObj", user);
		System.out.println(selectList);
		session.close();
	}
	
	@Test
	public void test01() throws Exception {
		String configPath="mydb-config.xml";
		SqlSession session = new SqlSessionFactory(configPath).opsession();
		System.out.println("=");
//		List<Object> selectList = session.selectList("select * from user", null);
		List<Object> selectList = session.selectList("selectAllUser", null);
		System.out.println(selectList);
	}
}
