package com.six.mydb;

import java.util.Map;
import java.util.Properties;

public class Config {
	private Properties setProperties;
	private Map<String, String> setMap;
	private Environment environment;
	private Map<String, MapStatement> sqlMap;

	public Map<String, MapStatement> getSqlMap() {
		return sqlMap;
	}

	public void setSqlMap(Map<String, MapStatement> sqlMap) {
		this.sqlMap = sqlMap;
	}

	public Environment getEnvironment() {
		return environment;
	}

	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	public Map<String, String> getSetMap() {
		return setMap;
	}

	public void setSetMap(Map<String, String> setMap) {
		this.setMap = setMap;
	}

	public Properties getSetProperties() {
		return setProperties;
	}

	public void setSetProperties(Properties setProperties) {
		this.setProperties = setProperties;
	}

}
