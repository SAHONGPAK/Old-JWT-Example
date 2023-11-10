package com.example.jwt.model.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.jwt.model.dto.UserDto;

@Mapper
public interface UserDao {
	UserDto selectByUser(UserDto userDto);
	UserDto selectById(String id);
	int insert(UserDto userDto);
	int update(Map<String, String> map);
	int delete(String userId);
}
