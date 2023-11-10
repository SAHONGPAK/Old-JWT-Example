package com.example.jwt.model.service;

import java.util.Map;

import com.example.jwt.model.dto.UserDto;

public interface UserService {
	UserDto login(UserDto userDto);
	UserDto myPage(String id);
	int signUp(UserDto userDto);
	boolean modify(Map<String, String> map);
	int withdrawal(String string);
}
