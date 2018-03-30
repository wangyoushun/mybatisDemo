package com.six.mydb;


import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.six.mydb.entity.User;



public class Main {


	
	@Test
	public void testParamList() throws Exception {
		String configPath="mydb-config.xml";
		SqlSession session = new SqlSessionFactory(configPath).opsession();
		System.out.println("=");
		List<String> arrayList = new ArrayList<>();
		arrayList.add("aaa");
		List<Object> selectList = session.selectList("com.six.domain.User.selectUserByList", arrayList);
		System.out.println(selectList);
	}
	
	
	public static void main(String[] args) throws Exception {
//		test011();
//		test02();
//		test03();
//		test04();
//		String configPath="mydb-config.xml";
//		SqlSession session = new SqlSessionFactory(configPath).opsession();
//		Map<String,Object> map = new HashMap<String,Object>();
//		map.put("id", 1);
//		
//		List<Object> selectList = session.selectList("com.six.domain.User.selectif", map);
//		System.out.println(selectList);
//		session.close();
		String configPath="mydb-config.xml";
		SqlSession session = new SqlSessionFactory(configPath).opsession();
	    HashMap<String, Object> hashMap = new HashMap<>();
	    hashMap.put("name", "aaa");
	    hashMap.put("type", 4);
		List<Object> selectList = session.selectList("queryAdmin", hashMap);
		System.out.println(selectList);
		
		
		
	}

	private static void test04() throws Exception, SQLException {
		String configPath="mydb-config.xml";
		SqlSession session = new SqlSessionFactory(configPath).opsession();
		List<Object> selectList = session.selectList("com.six.domain.User.selectResultObj", null);
		System.out.println(selectList);
		session.close();
	}

	private static void test03() throws Exception, SQLException {
		String configPath="mydb-config.xml";
		SqlSession session = new SqlSessionFactory(configPath).opsession();
		List<Object> selectList = session.selectList("com.six.domain.User.selectByNoparam", null);
		System.out.println(selectList);
		session.close();
	}

	private static void test02() throws Exception, SQLException {
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
