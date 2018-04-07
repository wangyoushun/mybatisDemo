package com.six.mydb;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.six.mydb.entity.User;
import com.six.mydb.exceptions.MyDBExeceptions;
import com.six.mydb.mapper.UserMapper;
import com.six.mydb.session.SqlSessionFactory;
import com.six.mydb.session.SqlSessionImpl;
import com.six.mydb.utils.LogKit;

public class Main {

	private static Logger logger = Logger.getLogger(Main.class);

	private static String configPath;

	public static void main(String[] args) throws Exception {
		selectMapperTest();

	}

	private static void selectMapperTest() {
		SqlSessionImpl session = getSession();
		UserMapper usermaper = (UserMapper) session.getMapper(UserMapper.class);
		List<User> queryForUser = usermaper.queryForUser();
		for (User user : queryForUser) {
			System.out.println(user);
		}
	}

	private static void logTest() throws Exception {
		String path4 = ClassLoader.getSystemClassLoader()
				.getResource("mydb-config.xml").toString();
		System.out.println(path4);
		LogKit.debug("sdfsdf");
		selectPage(1);
	}

	private static void selectPage(int a) throws Exception {
		List<User> list = druidAndResultTypeTest();
		SqlSessionImpl session = getSession();
		Page page = new Page();
		page.setPage(a);
		List<User> rs = session.selectListPage("queryForUser", null, page);
		for (User user : rs) {
			System.out.println(user);
		}
	}

	// 测试根据主键批量删除
	private static void deleteBatchByIds() throws Exception {
		List<User> list = druidAndResultTypeTest();
		SqlSessionImpl session = getSession();
		int deleteBatchByIds = session.deleteBatchByIds(list, 40);
		System.out.println(deleteBatchByIds);
	}

	// 测试循环插入的效率
	private static void insertFor() {
		long start = System.currentTimeMillis();
		SqlSessionImpl session = getSession();
		for (int i = 0; i < 1000; i++) {
			User user = new User();
			user.setName("aabbcc" + i);
			user.setAddress("上hi" + i);
			user.setAge(i % 10);
			user.setUserAccount("18882939");
			session.insert("insertUserBatch", user);
		}
		long end = System.currentTimeMillis();
		System.out.println(end - start);
	}

	// 批量插入
	private static void insertBatchTest() {
		long start = System.currentTimeMillis();
		SqlSessionImpl session = getSessionByDruid();
		ArrayList<User> list = new ArrayList<User>();
		for (int i = 0; i < 1000; i++) {
			User user = new User();
			user.setName("aabbcc" + i);
			user.setAddress("上hi" + i);
			user.setAge(i % 10);
			user.setUserAccount("18882939");
			list.add(user);
		}
		int insertBatch = session.insertBatch("insertUserBatch", list, 40);
		System.out.println(insertBatch);
		long end = System.currentTimeMillis();
		System.out.println(end - start);
	}

	private static List<User> druidAndResultTypeTest() throws Exception {
		SqlSessionImpl session = getSessionByDruid();
		List<User> selectList = session.selectList("queryForUser");
		for (User user : selectList) {
			System.out.println(user);
		}
		return selectList;
	}

	private static void deleteObjTest() {
		// 面向对象 删除
		SqlSessionImpl session = getSession();
		User user = new User();
		user.setId(2325);

		session.deleteById(user);
		session.close();
	}

	// 面向对象 更新
	private static void updateObjTest() {
		SqlSessionImpl session = getSession();
		User user = new User();
		user.setAddress("234");
		user.setName("zhang");
		user.setId(2325);

		session.updateById(user);
		session.close();
	}

	// 面向对象 插入
	private static void insertObj() {
		SqlSessionImpl session = getSession();
		User user = new User();
		user.setAddress("abc");
		user.setName("王");

		session.insert(user);
		session.close();
	}

	// 测试传入 单个参数 (基本类型+string)
	private static void deleteByIDTest() {
		SqlSessionImpl session = getSession();
		int id = 12;
		int delete = session.delete("deleteUserByID", id);
		System.out.println(delete);
	}

	// 测试事物和回滚
	private static void transactionTest() {
		SqlSessionImpl session = getSession();
		try {
			session.start();
			for (int i = 0; i < 10; i++) {
				int insert = session.insert("insertUser");
				System.out.println("insert--" + insert);
			}
			// int k = 1 / 0;
			session.insert("insertUser");
			session.commit();

		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		}
	}

	private static void selectByForTest() throws Exception {
		SqlSessionImpl session = getSession();

		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		ArrayList<Integer> idList = new ArrayList<Integer>();
		idList.add(3);
		idList.add(6);

		hashMap.put("idList", idList);

		List<User> selectList = session.selectList("selectByFor", hashMap);
		System.out.println(selectList);
	}

	private static SqlSessionImpl getSession() {
		SqlSessionImpl session = null;
		try {
			configPath = "mydb-config.xml";
			session = new SqlSessionFactory(configPath).opsession();
		} catch (Exception e) {
			throw new MyDBExeceptions();
		}
		return session;
	}

	private static SqlSessionImpl getSessionByDruid() {
		SqlSessionImpl session = null;
		try {
			configPath = "mydb-config.xml";
			session = new SqlSessionFactory(configPath, "druid").opsession();
		} catch (Exception e) {
			throw new MyDBExeceptions();
		}
		return session;
	}

	// sql 待参数更新测试
	private static void updateUserByParamTest() throws Exception {
		String configPath = "mydb-config.xml";
		SqlSessionImpl session = new SqlSessionFactory(configPath).opsession();
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
		SqlSessionImpl session = new SqlSessionFactory(configPath).opsession();
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
		SqlSessionImpl session = new SqlSessionFactory(configPath).opsession();
		int insert = session.insert("insertUser");
		System.out.println("insert--" + insert);
	}

	// sql 测试查询
	private static void testSelect() throws Exception {
		String configPath = "mydb-config.xml";
		SqlSessionImpl session = new SqlSessionFactory(configPath).opsession();
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("name", "aaa");
		hashMap.put("type", 4);
		List<Object> selectList = session.selectList("queryAdmin", hashMap);
		System.out.println(selectList);
	}

	@Test
	public void testParamList() throws Exception {
		String configPath = "mydb-config.xml";
		SqlSessionImpl session = new SqlSessionFactory(configPath).opsession();
		System.out.println("=");
		List<String> arrayList = new ArrayList<>();
		arrayList.add("aaa");
		List<Object> selectList = session.selectList(
				"com.six.domain.User.selectUserByList", arrayList);
		System.out.println(selectList);
	}

	private static void test04() throws Exception, SQLException {
		String configPath = "mydb-config.xml";
		SqlSessionImpl session = new SqlSessionFactory(configPath).opsession();
		List<Object> selectList = session.selectList(
				"com.six.domain.User.selectResultObj", null);
		System.out.println(selectList);
		session.close();
	}

	private static void test03() throws Exception, SQLException {
		String configPath = "mydb-config.xml";
		SqlSessionImpl session = new SqlSessionFactory(configPath).opsession();
		List<Object> selectList = session.selectList(
				"com.six.domain.User.selectByNoparam", null);
		System.out.println(selectList);
		session.close();
	}

	private static void test02() throws Exception, SQLException {
		String configPath = "mydb-config.xml";
		SqlSessionImpl session = new SqlSessionFactory(configPath).opsession();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", 2308);
		map.put("name", "叶小民1");
		List<Object> selectList = session.selectList("selectUserByMap", map);
		System.out.println(selectList);
		session.close();
	}

	private static void test011() throws Exception, SQLException {
		String configPath = "mydb-config.xml";
		SqlSessionImpl session = new SqlSessionFactory(configPath).opsession();
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
		SqlSessionImpl session = new SqlSessionFactory(configPath).opsession();
		System.out.println("=");
		// List<Object> selectList = session.selectList("select * from user",
		// null);
		List<Object> selectList = session.selectList("selectAllUser", null);
		System.out.println(selectList);
	}
}
