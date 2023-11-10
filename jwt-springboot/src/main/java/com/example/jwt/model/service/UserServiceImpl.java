package com.example.jwt.model.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.jwt.model.dao.UserDao;
import com.example.jwt.model.dto.UserDto;

@Service
public class UserServiceImpl implements UserService {

	private final UserDao userDao;
	
	private UserServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}
	
	@Override
	public UserDto login(UserDto userDto) {
		return userDao.selectByUser(userDto);
	}

	@Override
	public UserDto myPage(String id) {
		return userDao.selectById(id);
	}

	@Override
	public int signUp(UserDto userDto) {
		return userDao.insert(userDto);
	}

	@Override
	public boolean modify(Map<String, String> map) {
		int result = userDao.update(map);

		return result == 1? true : false;
	}

	@Override
	public int withdrawal(String userId) {
		return userDao.delete(userId);
	}

}
