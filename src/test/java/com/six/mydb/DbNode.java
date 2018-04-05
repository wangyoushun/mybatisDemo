package com.six.mydb;

public class DbNode {
	private String nodeType;
	private String value;
	private DbNode dbNode;
	
	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public DbNode getDbNode() {
		return dbNode;
	}

	public void setDbNode(DbNode dbNode) {
		this.dbNode = dbNode;
	}

	@Override
	public String toString() {
		return "DbNode [nodeType=" + nodeType + ", value=" + value + "]";
	}

}
