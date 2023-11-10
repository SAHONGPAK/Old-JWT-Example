package com.example.jwt.model.service;

import java.util.List;

import com.example.jwt.model.dto.BoardDto;

public interface BoardService {
	List<BoardDto> getAll();
	List<BoardDto> searchByTitle(String keyword);
	List<BoardDto> searchByContents(String keyword);
	List<BoardDto> searchByUserName(String keyword);
	BoardDto detail(int id);
	int write(BoardDto articleDto);
	BoardDto modify(BoardDto articleDto);
	int remove(int id);
}
