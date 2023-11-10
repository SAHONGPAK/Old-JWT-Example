package com.example.jwt.model.service;

import org.springframework.stereotype.Service;

import com.example.jwt.model.dao.AuthDao;
import com.example.jwt.model.dto.AuthDto;

@Service
public class AuthServiceImpl implements AuthService {

	private final AuthDao authDao;
	
	private AuthServiceImpl(AuthDao jwtDao) {
		this.authDao = jwtDao;
	}
	
	@Override
	public int setRefreshToken(AuthDto authDto) {
		return authDao.insert(authDto);
	}
	
	@Override
	public AuthDto getRefreshToken(String userId) {
		return authDao.select(userId);
	}
	
	@Override
	public int updateRefreshToken(AuthDto authDto) {
		return authDao.update(authDto);
	}
	
}
