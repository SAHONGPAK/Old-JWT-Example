package com.example.jwt.model.dto;

public class UserDto {
	private String id;
	private String password;
	private String name;
	private int permission;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPermission(int permission) {
		this.permission = permission;
	}
	public int getPermission() {
		return permission;
	}
	
	@Override
	public String toString() {
		return "UserDto [id=" + id + ", password=" + password + ", name=" + name + ", permission=" + permission + "]";
	}
}
