package com.six.mydb;

import java.beans.PropertyVetoException;
import java.sql.Connection;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class SqlSessionFactory {

	private ComboPooledDataSource dataSource = new ComboPooledDataSource();
	private Connection conn;
	private String resources;
	
	
	public SqlSessionFactory(String resources) {
		this.resources = resources;
	}

	public SqlSession opsession() throws Exception {
		XMLParser xmlParser = new XMLParser();
		Config config = xmlParser.parserXml(resources);
		Environment environment = config.getEnvironment();
		try {
			dataSource.setDriverClass(environment.getDriver());
			dataSource.setJdbcUrl(environment.getUrl());
			dataSource.setUser(environment.getUsername());
			dataSource.setPassword(environment.getPassword());

			conn = dataSource.getConnection();
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		return new SqlSession(config, conn);
	}
	
}
