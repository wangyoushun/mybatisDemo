package com.six.mydb;

public class MapStatement {
	private String sqlStr;
	private String parameterType;
	private String resultType;
	private String id;
	private String warpSql;

	public String getSqlStr() {
		return sqlStr;
	}

	public void setSqlStr(String sqlStr) {
		this.sqlStr = sqlStr;
	}

	public String getParameterType() {
		return parameterType;
	}

	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}

	public String getResultType() {
		return resultType;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWarpSql() {
		return warpSql;
	}

	public void setWarpSql(String warpSql) {
		this.warpSql = warpSql;
	}

	@Override
	public String toString() {
		return "MapStatement [sqlStr=" + sqlStr + ", parameterType="
				+ parameterType + ", resultType=" + resultType + ", id=" + id
				+ ", warpSql=" + warpSql + "]";
	}

}
