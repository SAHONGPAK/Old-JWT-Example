package com.example.jwt.controller;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwt.annotation.AuthRequired;
import com.example.jwt.model.dto.AuthDto;
import com.example.jwt.model.dto.UserDto;
import com.example.jwt.model.service.AuthService;
import com.example.jwt.model.service.UserService;
import com.example.jwt.util.JWTUtil;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
	
	@Value("${jwt.refreshtoken.expiretime}")
	private Integer refreshTokenExpireTime;
	
	private final UserService userService;
	private final JWTUtil jwtUtil;
	private final AuthService authService;
	
	private UserController(UserService userService, JWTUtil jwtUtil, AuthService authService) {
		this.userService = userService;
		this.jwtUtil = jwtUtil;
		this.authService = authService;
	}
	
	
	@ApiOperation(value="로그인", notes="로그인 성공시 JWT를 반환합니다.")
	@ApiResponses({
		// https://stackoverflow.com/questions/11714485/restful-login-failure-return-401-or-custom-response
		@ApiResponse(responseCode = "201", description = "로그인 성공"),
		@ApiResponse(responseCode = "401", description = "로그인 실패")
	})
	@ResponseBody
	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@RequestBody UserDto userDto, HttpServletResponse response) throws UnsupportedEncodingException {
		Map<String, Object> result = new HashMap<String, Object>();
		
		UserDto validUser = userService.login(userDto);
		
		// 일치하는 사용자가 없는 경우.
		if(validUser == null) {
			result.put("message", "아이디 또는 패스워드를 확인해주세요.");
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.UNAUTHORIZED);
		}
		
		// AccessToken, RefreshToken 발급
		// https://velog.io/@yaytomato/%ED%94%84%EB%A1%A0%ED%8A%B8%EC%97%90%EC%84%9C-%EC%95%88%EC%A0%84%ED%95%98%EA%B2%8C-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EC%B2%98%EB%A6%AC%ED%95%98%EA%B8%B0
		String accessToken = jwtUtil.createAccessToken(validUser.getId());
		String refreshToken = jwtUtil.createRefreshToken(validUser.getId());
		
		// RefreshToken은 HttpOnly Cookie로 발급
		Cookie cookie = new Cookie("refreshToken", refreshToken);
		cookie.setMaxAge(refreshTokenExpireTime);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		response.addCookie(cookie);
		
		// 이미 발급 받은 refreshToken이 있는지 확인.
		AuthDto authDto = authService.getRefreshToken(validUser.getId());
		
		if(authDto != null) {
			authDto.setRefreshToken(refreshToken);
			authService.updateRefreshToken(authDto);
		}
		else {
			// 발급받은 refreshToken을 DB에 저장.
			authService.setRefreshToken(new AuthDto(validUser.getId(), refreshToken));
		}
		
		// AccessToken은 JSON으로 전달
		result.put("accessToken", accessToken);
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.CREATED);
	}
	
	@ApiOperation(value="로그아웃", notes="해당 사용자의 refresh token을 삭제합니다.")
	@ApiResponses({
		// https://stackoverflow.com/questions/11714485/restful-login-failure-return-401-or-custom-response
		@ApiResponse(responseCode = "200", description = "로그아웃 성공"),
		@ApiResponse(responseCode = "401", description = "로그아웃 실패")
	})
	@ResponseBody
	@DeleteMapping("/logout")
	public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, ParseException {
		Map<String, Object> result = new HashMap<String, Object>();
		
		String userId = jwtUtil.getUserId(request.getHeader("Authorization"));
		
		// RefreshToken은 HttpOnly Cookie로 발급
		Cookie cookie = new Cookie("refreshToken", "");
		cookie.setMaxAge(0);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		response.addCookie(cookie);
		
		authService.updateRefreshToken(new AuthDto(userId, ""));
		
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
	
	
	@ApiOperation(value="회원가입", notes="회원가입")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "회원가입 성공"),
		// https://stackoverflow.com/questions/26587082/http-status-code-for-username-already-exists-when-registering-new-account
		@ApiResponse(responseCode = "409", description = "아이디 중복")
	})
	@ResponseBody
	@PostMapping("/signUp")
	public ResponseEntity<Map<String, Object>> signUp(@RequestBody UserDto userDto) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		userService.signUp(userDto);
		
		result.put("message", "회원가입이 완료되었습니다.");
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.CREATED);
	}
	
	
	@ApiOperation(value="마이페이지", notes="사용자 정보를 반환합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "사용자 정보 반환 성공"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "404", description = "사용자 정보 없음")
	})
	@ResponseBody
	@AuthRequired
	@GetMapping("/myPage")
	public ResponseEntity<Map<String, Object>> myPage(HttpServletRequest request, HttpServletResponse response) throws ParseException {
		Map<String, Object> result = new HashMap<String, Object>();
		
		
		// Authorization에 포함된 accessToken의 userId를 가져온다.
		String userId = jwtUtil.getUserId(request.getHeader("Authorization"));
		
		Cookie[] cookieList = request.getCookies();
		
		String requestRefreshToken = "";
		
		for(Cookie cookie : cookieList) {
			if(cookie.getName().equals("refreshToken")) {
				requestRefreshToken = cookie.getValue();
			}
		}
		
		// 요청으로 들어온 refreshToken이 Database의 refreshToken과 일치하는지 확인.
		String dbRefreshToken = authService.getRefreshToken(userId).getRefreshToken();
		
		if(!requestRefreshToken.equals(dbRefreshToken)) {
			// RefreshToken은 HttpOnly Cookie로 발급
			Cookie cookie = new Cookie("refreshToken", "");
			cookie.setMaxAge(0);
			cookie.setHttpOnly(true);
			cookie.setPath("/");
			response.addCookie(cookie);
			
			result.put("message", "로그인이 필요합니다.");
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.FORBIDDEN);
		}
		
		// 해당 userId의 정보를 획득.
		UserDto user = userService.myPage(userId);
		
		// 사용자 정보 중 비밀번호는 전송하지 말아야한다.
		user.setPassword("");
		
		result.put("userInfo", user);
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
	
	@ApiOperation(value="회원 정보 수정", notes="사용자 정보를 수정합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "사용자 정보 수정 성공"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "404", description = "사용자 정보 없음")
	})
	@ResponseBody
	@AuthRequired
	@PutMapping("/modify")
	public ResponseEntity<Map<String, Object>> modify(@RequestBody Map<String, String> requestMap, HttpServletRequest request) throws ParseException {
		Map<String, Object> result = new HashMap<String, Object>();
		
		// accessToken에 저장된 userId를 획득.
		String accessTokenUserId = jwtUtil.getUserId(request.getHeader("Authorization"));
		String requestUserId = requestMap.get("id");
		
		if(!requestUserId.equals(accessTokenUserId)) {
			result.put("message", "권한이 없습니다.");
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.FORBIDDEN);
		}
		
		
		boolean isUpdated = userService.modify(requestMap);
		
		if(!isUpdated) {
			result.put("message", "일치하는 사용자가 없습니다.");
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.NOT_FOUND);
		}
		
		
		
		
		result.put("message", "회원 정보가 수정되었습니다. 다시 로그인해주세요.");
		authService.updateRefreshToken(new AuthDto(accessTokenUserId, ""));
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
	
	@ApiOperation(value="회원 정보 삭제", notes="사용자 정보를 삭제합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "사용자 정보 삭제 성공"),
		@ApiResponse(responseCode = "403", description = "권한 없음"),
		@ApiResponse(responseCode = "404", description = "사용자 정보 없음")
	})
	@ResponseBody
	@AuthRequired
	@DeleteMapping("/withdrawal")
	public ResponseEntity<Map<String, Object>> withdrawal() {
		userService.withdrawal("");
		return null;
	}
	 
}
