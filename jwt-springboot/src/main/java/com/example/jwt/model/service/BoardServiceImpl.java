package com.example.jwt.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.jwt.model.dao.BoardDao;
import com.example.jwt.model.dto.BoardDto;

@Service
public class BoardServiceImpl implements BoardService {

	private final BoardDao boardDao;
	
	private BoardServiceImpl(BoardDao boardDao) {
		this.boardDao = boardDao;
	}
	
	@Override
	public List<BoardDto> getAll() {
		return boardDao.selectAll();
	}

	@Override
	public List<BoardDto> searchByTitle(String keyword) {
		return boardDao.selectByTitle(keyword);
	}

	@Override
	public List<BoardDto> searchByContents(String keyword) {
		return boardDao.selectByContents(keyword);
	}

	@Override
	public List<BoardDto> searchByUserName(String keyword) {
		return boardDao.selectByUserName(keyword);
	}

	@Override
	public BoardDto detail(int id) {
		return boardDao.selectById(id);
	}

	@Override
	public int write(BoardDto boardDto) {
		return boardDao.insert(boardDto);
	}

	@Override
	public BoardDto modify(BoardDto boardDto) {
		return boardDao.update(boardDto);
	}

	@Override
	public int remove(int id) {
		return boardDao.delete(id);
	}

}
