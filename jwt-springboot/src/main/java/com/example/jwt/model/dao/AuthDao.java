package com.example.jwt.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.example.jwt.model.dto.AuthDto;

@Mapper
public interface AuthDao {
	AuthDto select(String userId);
	int insert(AuthDto authDto);
	int update(AuthDto authDto);
}
