package com.six.demo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.six.mydb.utils.PropertiesUtil;

public class DruidTest {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		Properties properties = PropertiesUtil.newInstance("datasource.properties");
		System.out.println(properties);

		DataSource dataSource = DruidDataSourceFactory.createDataSource(properties);
		Connection connection = dataSource.getConnection();
		Statement createStatement = connection.createStatement();
		ResultSet rs = createStatement.executeQuery("select * from user");
		int n = rs.getMetaData().getColumnCount();
		while (rs.next()) {

			for (int i = 1; i < n; i++) {
				Object object = rs.getObject(i);

				System.out.println(object);
			}

		}

	}
}
