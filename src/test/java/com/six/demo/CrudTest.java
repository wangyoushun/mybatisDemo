package com.six.demo;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ognl.Ognl;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.six.domain.Score;
import com.six.domain.User;

public class CrudTest {

	private Logger logger = Logger.getLogger(CrudTest.class);
	SqlSession openSession;
	SqlSessionFactory sqlSessionFactory;

	
	
	@Test
	public void testPageHelp2() throws Exception {
		openSession = sqlSessionFactory.openSession();
		Page<Object> startPage = PageHelper.startPage(1, 4);
		PageHelper.orderBy("t3.id asc");
		List<Map> selectList = openSession
				.selectList("com.six.domain.User.selectUserScore");
		for(int i=0; i<selectList.size(); i++){
			System.out.println(selectList.get(i));
		}
	
		openSession.close();

	}
	
	
	// 传统for循环插入数据时间
	@Test
	public void testBathSave() throws Exception {
		long start = System.currentTimeMillis();
		openSession = sqlSessionFactory.openSession();

		List<User> userList = new ArrayList<User>(1000);
		for (int i = 0; i < 1000; i++) {
			User user = new User();
			user.setAddress("address" + i);
			user.setAge(i);
			user.setName("name" + i);
			userList.add(user);
		}

		openSession.insert("com.six.domain.User.saveBatch", userList);
		openSession.commit();
		openSession.close();

		long end = System.currentTimeMillis();
		System.out.println("inset cost time " + (end - start));
	}

	// 测试多个数据源
	@Test
	public void testDataSource() throws Exception {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
		sqlSessionFactory = sqlSessionFactoryBuilder.build(inputStream,
				"development_oracle");
		openSession = sqlSessionFactory.openSession();
		List<Map> selectList = openSession
				.selectList("com.six.domain.User.selectBag");
		System.out.println(selectList);
		String s = (String) selectList.get(0).get("BAG_ID");
		System.out.println(s);
	}

	// 分页插件 PageHelper
	@Test
	public void testPageHelp() throws Exception {
		openSession = sqlSessionFactory.openSession();
		Page<Object> startPage = PageHelper.startPage(1, 4);
		List<User> selectList = openSession
				.selectList("com.six.domain.User.selectAllUser");
		System.out.println(selectList);
		System.out.println(selectList.get(0));
		openSession.close();

	}

	// 分页
	// mybatis自带分页 java.sql.ResultSet.absolute(int)
	// 通过jdbc的ResultSet的对象rs时用rs的游标定位到offset的位置
	// 查询的还是符合条件的全部数据，只是利用游标进行定位了
	@Test
	public void testPageQuery() throws Exception {
		openSession = sqlSessionFactory.openSession();
		RowBounds rowBounds = new RowBounds(1, 4);
		User user = new User();
		user.setName("叶小名");
		List<User> selectList = openSession.selectList(
				"com.six.domain.User.selectAllUser", user, rowBounds);
		System.out.println(selectList);
		openSession.close();

	}

	// ${} 参数
	@Test
	public void test$2() throws Exception {
		openSession = sqlSessionFactory.openSession();
		Map<String, Object> map = new HashMap<String, Object>();
		User user = new User();
		user.setName("叶小名");
		map.put("user", user);
		map.put("order", " id desc ");
		List<User> selectList = openSession.selectList(
				"com.six.domain.User.selectParam2", map);
		System.out.println(selectList);
		openSession.close();
	}

	// ${} 参数
	@Test
	public void test$() throws Exception {
		openSession = sqlSessionFactory.openSession();
		User user = new User();
		user.setName(" id desc");
		List<User> selectList = openSession.selectList(
				"com.six.domain.User.selectParam", user);
		System.out.println(selectList);
		openSession.close();
	}

	// 测试一下ognl表达式
	@SuppressWarnings("unused")
	@Test
	public void testOgnl() throws Exception {
		String expression = "name!=null and name!=''";
		Object parseExpression = Ognl.parseExpression(expression);
	}

	// for more param
	@Test
	public void testFor() throws Exception {
		openSession = sqlSessionFactory.openSession();
		Integer[] ids = new Integer[] { 1, 4 };
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "叶小名");
		map.put("ids", ids);

		List<User> selectList = openSession.selectList(
				"com.six.domain.User.selectFor", map);
		System.out.println(selectList);

		openSession.close();
	}

	// for array
	@Test
	public void testForArray() throws Exception {
		openSession = sqlSessionFactory.openSession();
		Integer[] ids = new Integer[] { 1, 4 };
		List<User> selectList = openSession.selectList(
				"com.six.domain.User.selectForArray", ids);
		System.out.println(selectList);
		openSession.close();
	}

	// for list test 分页  pageHelper
	@Test
	public void testForList2() throws Exception {
		openSession = sqlSessionFactory.openSession();
		Map<String,Object> map = new HashMap<String,Object>();
		
		List<User> list2 = new ArrayList<User>();
		User user1 = new User();
		user1.setId(2307);
		User user2 = new User();
		user2.setId(2308);
		User user3 = new User();
		user3.setId(2309);
		User user4 = new User();
		user4.setId(2310);
		
		list2.add(user1);
		list2.add(user2);
		list2.add(user3);
		list2.add(user4);

		map.put("list", list2);
		PageHelper.startPage(1,2);
		List<User> selectList = openSession.selectList(
				"com.six.domain.User.selectForListPage", map);
		System.out.println(selectList);
		
		openSession.close();
	}
	
	//自定义分页插件
	// for list
	@Test
	public void testForList() throws Exception {
		openSession = sqlSessionFactory.openSession();
		
		Pager page = new Pager();
		page.setCurrentPage(1);
		page.setPageSize(2);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("page", page);
		
		List<User> list2 = new ArrayList<User>();
		User user1 = new User();
		user1.setId(2307);
		User user2 = new User();
		user2.setId(2308);
		User user3 = new User();
		user3.setId(2309);
		User user4 = new User();
		user4.setId(2310);
		
		list2.add(user1);
		list2.add(user2);
		list2.add(user3);
		list2.add(user4);

		map.put("list", list2);
		List<User> selectList = openSession.selectList(
				"com.six.domain.User.selectForListPage", map);
		System.out.println(selectList);
		page.setPageSize(selectList.size());
		System.out.println(page);
		openSession.close();
	}

	// session内的一级缓存
	@Test
	public void testCache() throws Exception {
		openSession = sqlSessionFactory.openSession();
		User testUserById = testUserById();
		User testUserById2 = testUserById();
		System.out.println(testUserById);
		System.out.println(testUserById2);
		System.out.println(testUserById == testUserById2);
		openSession.close();
	}

	public User testUserById() throws Exception {
		Integer id = 1;
		User user = new User();
		user.setId(id);
		User u1 = openSession.selectOne("com.six.domain.User.selectUserById",
				user);
		return u1;
	}

	@Test
	public void testInsert2() throws Exception {
		openSession = sqlSessionFactory.openSession(ExecutorType.BATCH,false);
		for (int i = 0; i < 100; i++) {
			User user = new User();
			user.setName("叶小民" + i);
			openSession.insert("com.six.domain.User.saveUser", user);
		}
		openSession.commit();
		openSession.close();
	}

	// insert
	@Test
	public void testInsert() throws Exception {
		openSession = sqlSessionFactory.openSession();
		User user = new User();
		// user.setAge(23);
		user.setName("叶小名2");
		int insert = openSession.insert("com.six.domain.User.saveUser", user);
		System.out.println(insert);
		openSession.commit();
		openSession.close();
	}
	
	// insert
	@Test
	public void testInsert3() throws Exception {
		openSession = sqlSessionFactory.openSession();
		Score score = new Score();
		score.setUserId(2308);
		score.setScore(121);
		int insert = openSession.insert("com.six.domain.User.saveScore", score);
		System.out.println(insert);
		openSession.commit();
		openSession.close();
	}

	// 测试resulttype
	@Test
	public void testResultTypeMap() throws Exception {
		openSession = sqlSessionFactory.openSession();
		Integer id = 1;
		User user = new User();
		user.setId(id);
		List<User> selectList = openSession.selectList(
				"com.six.domain.User.selectUserHashMap", user);
		Map user2 = (Map) selectList.get(0);
		System.out.println(user2);
		System.out.println(selectList);
		openSession.close();
	}

	// 测试resultmap
	@Test
	public void testResultMap() throws Exception {

		openSession = sqlSessionFactory.openSession();
		Integer id = 1;
		User user = new User();
		user.setId(id);
		List<User> selectList = openSession.selectList(
				"com.six.domain.User.selectAllUserResultMap", user);
		System.out.println(selectList);
		openSession.close();

	}

	// 测试ognl表达式 传参要提供get方法
	@Test
	public void testFindById() throws Exception {
		Integer id = 1;
		User user = new User();
		user.setId(id);
		// Map<String,Integer> map = new HashMap<String,Integer>();
		// map.put("id", id);
		List<User> selectList = openSession.selectList(
				"com.six.domain.User.selectUserById", user);
		System.out.println(selectList);
	}

	// 自定义ResultHandler
	@Test
	public void testResHandler() throws Exception {
		class MyResultHandler implements ResultHandler {
			public void handleResult(ResultContext resultContext) {
				Object resultObject = resultContext.getResultObject();
				System.out.println(resultObject);
			}
		}
		MyResultHandler myResultHandler = new MyResultHandler();
		openSession
				.select("com.six.domain.User.selectAllUser", myResultHandler);
	}

	@Test
	public void testFindAll() throws Exception {
		openSession = sqlSessionFactory.openSession();
		logger.info("log start");
		Pager page = new Pager();
		page.setCurrentPage(1);
		page.setPageSize(4);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("page", page);
		List<User> selectList = openSession
				.selectList("com.six.domain.User.selectAllUser",map);
		System.out.println(selectList);
		System.out.println(map.get("page"));
		openSession.close();
	}
	
	// init
	@Before
	public void init() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		System.out.println(sqlSessionFactory);
		// openSession = sqlSessionFactory.openSession();
		// logger.debug("session open------");
	}

	// @After
	public void close() {
		if (openSession != null) {
			openSession.close();
			logger.debug("session close-------");
		}
	}

	// 测试方法的执行时间
	@Test
	public void testMethodTime() throws Exception {
		// EnumNull in = EnumNull.IN;
		TimerUtil timerUtil = new TimerUtil();
		timerUtil.getTime();
	}
}
