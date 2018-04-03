package com.six.domain;

import java.util.List;

public class User {

	private Integer id;
	private String name;
	private String address;
	private Integer age;

	private List<String> list;
	
	private User u;
	
	
	
	
	public User getU() {
		return u;
	}

	public void setU(User u) {
		this.u = u;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

//	@Override
//	public String toString() {
//		return "User [id=" + id + ", name=" + name + ", address=" + address
//				+ ", age=" + age + "]";
//	}

}
