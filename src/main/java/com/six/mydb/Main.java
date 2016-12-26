package com.six.mydb;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;


public class Main {

	public static void main(String[] args) throws Exception {
		String configPath="mydb-config.xml";
		SqlSession session = new SqlSessionFactory(configPath).opsession();
	}
	
	@Test
	public void test01() throws Exception {
		String configPath="mydb-config.xml";
		SqlSession session = new SqlSessionFactory(configPath).opsession();
		System.out.println("=");
//		List<Object> selectList = session.selectList("select * from user", null);
//		System.out.println(selectList);
	}
}
