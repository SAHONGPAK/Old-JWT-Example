package com.example.jwt.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwt.annotation.AuthRequired;
import com.example.jwt.model.dto.BoardDto;
import com.example.jwt.model.dto.UserDto;
import com.example.jwt.model.service.BoardService;
import com.example.jwt.util.JWTUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/board")
public class BoardController {
	
	private final BoardService boardService;
	private final JWTUtil jwtUtil;
	
	private BoardController(BoardService boardService, JWTUtil jwtUtil) {
		this.boardService = boardService;
		this.jwtUtil = jwtUtil;
	}
	
	@ApiOperation(value="게시글 전체 목록", notes="게시글 전체 목록을 반환합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "사용자 정보 반환 성공"),
		@ApiResponse(responseCode = "403", description = "권한 없음")
	})
	@ResponseBody
	@AuthRequired
	@GetMapping("/list")
	public ResponseEntity<Map<String, Object>> list() {
		boardService.getAll();
		return null;
	}
	
	@ApiOperation(value="게시글 작성", notes="게시글을 작성합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "게시글 작성 완료"),
		@ApiResponse(responseCode = "403", description = "권한 없음")
	})
	@ResponseBody
	@AuthRequired
	@PostMapping("/write")
	public ResponseEntity<Map<String, Object>> write(@RequestBody BoardDto boardDto) {
		boardService.write(boardDto);
		return null;
	}
	
	@ApiOperation(value="게시글 상세", notes="게시글 번호에 따른 내용을 반환합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "게시글 반환 완료"),
		@ApiResponse(responseCode = "403", description = "권한 없음")
	})
	@ResponseBody
	@AuthRequired
	@GetMapping("/detail/{id}")
	public ResponseEntity<Map<String, Object>> detail(@PathVariable int id) {
		boardService.detail(id);
		return null;
	}
	
	@ApiOperation(value="게시글 수정", notes="게시글 번호에 따른 내용을 수정합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "게시글 수정 완료"),
		@ApiResponse(responseCode = "403", description = "권한 없음")
	})
	@ResponseBody
	@AuthRequired
	@PutMapping("/modify/{id}")
	public ResponseEntity<Map<String, Object>> modify(@RequestBody BoardDto boardDto) {
		boardService.modify(boardDto);
		return null;
	}
	
	@ApiOperation(value="게시글 삭제", notes="게시글 번호에 따른 내용을 삭제합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "게시글 삭제 완료"),
		@ApiResponse(responseCode = "403", description = "권한 없음")
	})
	@ResponseBody
	@AuthRequired
	@DeleteMapping("/remove/{id}")
	public ResponseEntity<Map<String, Object>> remove(@PathVariable int id) {
		boardService.remove(id);
		return null;
	}
}
