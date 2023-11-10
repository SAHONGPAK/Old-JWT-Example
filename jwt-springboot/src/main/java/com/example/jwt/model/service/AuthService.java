package com.example.jwt.model.service;

import com.example.jwt.model.dto.AuthDto;

public interface AuthService {
	int setRefreshToken(AuthDto authDto);
	AuthDto getRefreshToken(String userId);
	int updateRefreshToken(AuthDto authDto);
	
}
