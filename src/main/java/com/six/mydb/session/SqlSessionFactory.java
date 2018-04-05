package com.six.mydb.session;

import java.sql.Connection;

import com.alibaba.druid.pool.DruidDataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.six.mydb.config.Config;
import com.six.mydb.config.Environment;
import com.six.mydb.parser.XMLParser;

public class SqlSessionFactory {

	private Connection conn;
	private String resources;
	private String dataSourcesType;

	public SqlSessionFactory() {
	}

	public SqlSessionFactory(String resources) {
		this.resources = resources;
	}

	public SqlSessionFactory(String resources, String dataSourcesType) {
		this.resources = resources;
		this.dataSourcesType = dataSourcesType;
	}

	public SqlSessionImpl opsession() throws Exception {
		XMLParser xmlParser = new XMLParser();
		Config config = xmlParser.parserXml(resources);
		Environment environment = config.getEnvironment();

		if ("druid".equals(dataSourcesType)) {
			DruidDataSource dataSource = new DruidDataSource();
			dataSource.setDriverClassName(environment.getDriver());
			dataSource.setUrl(environment.getUrl());
			dataSource.setUsername(environment.getUsername());
			dataSource.setPassword(environment.getPassword());
			conn = dataSource.getConnection();
		} else {
			ComboPooledDataSource dataSource = new ComboPooledDataSource();
			dataSource.setDriverClass(environment.getDriver());
			dataSource.setJdbcUrl(environment.getUrl());
			dataSource.setUser(environment.getUsername());
			dataSource.setPassword(environment.getPassword());
			conn = dataSource.getConnection();
		}

		return new SqlSessionImpl(config, conn);
	}

}
