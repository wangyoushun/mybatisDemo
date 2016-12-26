package com.six.mydb;

public class Environment {
	private String id;
	private boolean autoCmit;
	private String driver;
	private String url;
	private String username;
	private String password;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isAutoCmit() {
		return autoCmit;
	}

	public void setAutoCmit(boolean autoCmit) {
		this.autoCmit = autoCmit;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Environment [id=" + id + ", autoCmit=" + autoCmit + ", driver="
				+ driver + ", url=" + url + ", username=" + username
				+ ", password=" + password + "]";
	}

}
