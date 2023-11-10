package com.example.jwt.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.jwt.model.dto.BoardDto;

@Mapper
public interface BoardDao {
	List<BoardDto> selectAll();
	List<BoardDto> selectByTitle(String keyword);
	List<BoardDto> selectByContents(String keyword);
	List<BoardDto> selectByUserName(String keyword);
	BoardDto selectById(int id);
	int insert(BoardDto articleDto);
	BoardDto update(BoardDto articleDto);
	int delete(int id);
}
