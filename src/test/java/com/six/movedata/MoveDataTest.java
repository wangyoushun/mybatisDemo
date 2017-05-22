package com.six.movedata;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.sql.DATE;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.six.domain.BagDetail;
import com.six.domain.BagInfo;

public class MoveDataTest {

	private Logger logger = Logger.getLogger(MoveDataTest.class);
	private String dest = "e://test.txt";
	private String date="2017-01-17";
	Map<String, String> typeMap = new HashMap<String, String>();

	{
		
		typeMap.put("VARCHAR2", "String");
		typeMap.put("DATE", "Date");

	}

	@Test
	public void testName4() throws Exception {
		logger.debug("2334");
	}

	//根据bagid保存detail数据
	@Test
	public void tesSaveBagDetail() throws Exception {
		long start = System.currentTimeMillis();
		int count=0;
		
		SqlSession sessionMysql = initMysql();
		SqlSession sessionOracle = initOracle();
		BagInfo bagInfo = new BagInfo();
		bagInfo.setAircraftDate(date);
		List<BagInfo> selectList = sessionMysql.selectList("dzpzd.selectBagMysql",
				bagInfo);
		for (BagInfo bag : selectList) {
			List<BagDetail> list = sessionOracle.selectList("dzpzd.selectDetailBybagid", bag);
			for (BagDetail bagDetail : list) {
				sessionMysql.insert("dzpzd.savesBagDetail", bagDetail);
				count++;
//				System.out.println(bagDetail);
			}
			if(count%500==0){
				sessionMysql.commit();
			}
		}
		sessionMysql.commit();
		close(sessionOracle);
		close(sessionMysql);
		
		long end = System.currentTimeMillis();
		System.out.println((end - start) / 1000 + "===cost time num: "
				+ count);
	}

	// 保存baginfo数据
	@Test
	public void testSaveBagInfo() throws Exception {
		long start = System.currentTimeMillis();
		SqlSession sqlSessionOracle = initOracle();
		BagInfo bagInfo = new BagInfo();
		bagInfo.setAircraftDate(date);
		// bagInfo.setAircraftNum("MU2850");
		List<BagInfo> selectList = sqlSessionOracle.selectList(
				"dzpzd.selectBag", bagInfo);
		System.out.println(selectList.size());
		System.out.println(selectList.get(0));
		close(sqlSessionOracle);

		SqlSession sqlSessionMysql = initMysql();
		for (BagInfo bag : selectList) {
			System.out.println(bag.getAircraftType());
			sqlSessionMysql.insert("dzpzd.savesBag", bag);
		}
		sqlSessionMysql.commit();
		close(sqlSessionMysql);
		long end = System.currentTimeMillis();
		System.out.println((end - start) / 1000 + "===cost time num: "
				+ selectList.size());
	}

	// 获取oracle sqlsession
	public SqlSession initOracle() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactoryOracle = new SqlSessionFactoryBuilder()
				.build(inputStream, "development_oracle");
		SqlSession sqlSessonOracle = sqlSessionFactoryOracle.openSession();
		logger.debug("oracle session open------");
		return sqlSessonOracle;
	}

	// 获取mysql sqlsession
	public SqlSession initMysql() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactoryMysql = new SqlSessionFactoryBuilder()
				.build(inputStream, "development");
		SqlSession sqlSessonMysql = sqlSessionFactoryMysql.openSession(ExecutorType.BATCH,false);
		logger.debug("mysql session open------");
		return sqlSessonMysql;
	}

	// @After
	public void close(SqlSession openSession) {
		if (openSession != null) {
			openSession.close();
			logger.debug("session close-------");
		}
	}

	//根据数据库转换xml jdbctype
	@Test
	public void testDataToXml() throws Exception {
		List<String> lines = FileUtil.lines(new File(dest));
		System.out.println(lines);
		String str="";
		for (String string : lines) {
			String[] split = string.split(" ");
			str=str+"#{"+StringUtil.underlineToCamel(split[0])+",jdbcType="+getType(split[1]).toUpperCase()+"},";
		}
		System.out.println(str);
	}
	
	// 根据数据库转化实体类
	@Test
	public void testDataToModel() throws Exception {
		List<String> lines = FileUtil.lines(new File(dest));
		System.out.println(lines);
		String name = "";
		String type = "";
		String content = "";
		for (String str : lines) {
			String[] split = str.split(" ");

			name = StringUtil.underlineToCamel(split[0]);
			type = getType(split[1]);
//			System.out.println(name + "--" + type);
			if (split.length >= 3) {
				content = split[2];
//				System.out.println(content);
			}
			print(name, type, content);

		}

	}

	//格式化打印字段
	private void print(String name, String type, String content) {
		String str="private "+typeMap.get(type)+" "+name+";";
		if(!"".equals(content)){
			str+="  //"+content;
		}
		System.out.println(str);
	}

	//获取数据库表字段类型
	public String getType(String string) {
		int indexOf = string.indexOf("(");
		if(indexOf==-1)
			return string;
		
		String substring = string.substring(0, indexOf);
		if("VARCHAR2".equals(substring))
			substring="VARCHAR";
		return substring;
	}
}
