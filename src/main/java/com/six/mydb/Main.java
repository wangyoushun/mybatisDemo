package com.six.mydb;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.six.mydb.entity.User;
import com.six.mydb.exceptions.MyDBExeceptions;

public class Main {

	private static String configPath;

	public static void main(String[] args) {
		transactionTest();

	}

	// 测试事物和回滚
	private static void transactionTest() {
		SqlSession session = getSession();
		try {
			session.start();
			for (int i = 0; i < 10; i++) {
				int insert = session.insert("insertUser");
				System.out.println("insert--" + insert);
			}
			int k = 1 / 0;
			session.insert("insertUser");
			session.commit();

		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
	}

	private static void selectByForTest() throws Exception {
		SqlSession session = getSession();

		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		ArrayList<Integer> idList = new ArrayList<Integer>();
		idList.add(3);
		idList.add(6);

		hashMap.put("idList", idList);

		List<User> selectList = session.selectList("selectByFor", hashMap);
		System.out.println(selectList);
	}

	private static SqlSession getSession() {
		SqlSession session = null;
		try {
			configPath = "mydb-config.xml";
			session = new SqlSessionFactory(configPath).opsession();
		} catch (Exception e) {
			throw new MyDBExeceptions();
		}
		return session;
	}

	// sql 待参数更新测试
	private static void updateUserByParamTest() throws Exception {
		String configPath = "mydb-config.xml";
		SqlSession session = new SqlSessionFactory(configPath).opsession();
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("id", 6);
		hashMap.put("age", 13);
		hashMap.put("userAccount", "1111111");

		int insert = session.insert("updateUserByParam", hashMap);
		System.out.println("update--" + insert);
	}

	// sql 带参数插入测试
	private static void insertUserByParamTest() throws Exception {
		String configPath = "mydb-config.xml";
		SqlSession session = new SqlSessionFactory(configPath).opsession();
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("name", "cc");
		hashMap.put("age", 13);
		hashMap.put("userAccount", "182739273");

		int insert = session.insert("insertUserByParam", hashMap);
		System.out.println("insert--" + insert);
	}

	// sql 插入测试
	private static void insertTest() throws Exception {
		String configPath = "mydb-config.xml";
		SqlSession session = new SqlSessionFactory(configPath).opsession();
		int insert = session.insert("insertUser");
		System.out.println("insert--" + insert);
	}

	// sql 测试查询
	private static void testSelect() throws Exception {
		String configPath = "mydb-config.xml";
		SqlSession session = new SqlSessionFactory(configPath).opsession();
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("name", "aaa");
		hashMap.put("type", 4);
		List<Object> selectList = session.selectList("queryAdmin", hashMap);
		System.out.println(selectList);
	}

	@Test
	public void testParamList() throws Exception {
		String configPath = "mydb-config.xml";
		SqlSession session = new SqlSessionFactory(configPath).opsession();
		System.out.println("=");
		List<String> arrayList = new ArrayList<>();
		arrayList.add("aaa");
		List<Object> selectList = session.selectList(
				"com.six.domain.User.selectUserByList", arrayList);
		System.out.println(selectList);
	}

	private static void test04() throws Exception, SQLException {
		String configPath = "mydb-config.xml";
		SqlSession session = new SqlSessionFactory(configPath).opsession();
		List<Object> selectList = session.selectList(
				"com.six.domain.User.selectResultObj", null);
		System.out.println(selectList);
		session.close();
	}

	private static void test03() throws Exception, SQLException {
		String configPath = "mydb-config.xml";
		SqlSession session = new SqlSessionFactory(configPath).opsession();
		List<Object> selectList = session.selectList(
				"com.six.domain.User.selectByNoparam", null);
		System.out.println(selectList);
		session.close();
	}

	private static void test02() throws Exception, SQLException {
		String configPath = "mydb-config.xml";
		SqlSession session = new SqlSessionFactory(configPath).opsession();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", 2308);
		map.put("name", "叶小民1");
		List<Object> selectList = session.selectList("selectUserByMap", map);
		System.out.println(selectList);
		session.close();
	}

	private static void test011() throws Exception, SQLException {
		String configPath = "mydb-config.xml";
		SqlSession session = new SqlSessionFactory(configPath).opsession();
		Integer id = 2308;
		// List<Object> selectList = session.selectList("selectAllUser", id);
		User user = new User();
		user.setName("叶小民1");
		user.setId(id);
		List<Object> selectList = session.selectList("selectUserByObj", user);
		System.out.println(selectList);
		session.close();
	}

	@Test
	public void test01() throws Exception {
		String configPath = "mydb-config.xml";
		SqlSession session = new SqlSessionFactory(configPath).opsession();
		System.out.println("=");
		// List<Object> selectList = session.selectList("select * from user",
		// null);
		List<Object> selectList = session.selectList("selectAllUser", null);
		System.out.println(selectList);
	}
}
