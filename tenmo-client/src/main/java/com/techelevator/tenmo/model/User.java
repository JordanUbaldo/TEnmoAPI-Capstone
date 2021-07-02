package com.techelevator.tenmo.model;

public class User {

	private Integer id;
	private String username;

	public Integer getId() {
		return id;
	}

	public String getUsernameById(int id) {
		return username;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
}
