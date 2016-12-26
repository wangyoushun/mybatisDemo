package com.six.mydb;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;


public class Main {

	@Test
	public void test01() throws Exception {
		String configPath="mydb-config.xml";
		SqlSession session = new SqlSessionFactory(configPath).opsession();
		List<Object> selectList = session.selectList("select * from user", null);
		System.out.println(selectList);
	}
}
