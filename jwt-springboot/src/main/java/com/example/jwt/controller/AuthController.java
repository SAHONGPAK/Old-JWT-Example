package com.example.jwt.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwt.annotation.AuthRequired;
import com.example.jwt.model.dto.AuthDto;
import com.example.jwt.model.service.AuthService;
import com.example.jwt.util.JWTUtil;

// https://velog.io/@cloud_oort/Next.js-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EA%B5%AC%ED%98%84-Silent-refresh

@RestController
@RequestMapping("/api/v1")
public class AuthController {
	
	@Value("${jwt.accesstoken.expiretime}")
	private int accessTokenExpireTime;
	
	@Value("${jwt.refreshtoken.expiretime}")
	private int refreshTokenExpireTime;
	
	private final JWTUtil jwtUtil;
	private final AuthService authService;
	
	private AuthController(JWTUtil jwtUtil, AuthService authService) {
		this.jwtUtil = jwtUtil;
		this.authService = authService;
	}
	
	@PostMapping("/slient-refresh")
	public ResponseEntity<Map<String, Object>> slientRefresh(HttpServletRequest request, HttpServletResponse response) throws ParseException, UnsupportedEncodingException {
		
		// slientRefresh가 발생하는 경우
		// HttpOnly Cookie를 가져온다.
		Cookie[] cookieList = request.getCookies();
		
		String refreshToken = "";
		
		for(Cookie cookie : cookieList) {
			if(cookie.getName().equals("refreshToken")) {
				refreshToken = cookie.getValue();
			}
		}
		
		String userId = jwtUtil.getUserId(refreshToken);
		String dbRefreshToken = authService.getRefreshToken(userId).getRefreshToken();
		
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		// Cookie에 담겨있는 refreshToken을 검증하고,
		boolean isValid = jwtUtil.vaildCheck(refreshToken);
	
		// 요청으로 넘어온 refreshToken과 Database에 저장된 refreshToken이 다른 경우.
		if(!refreshToken.equals(dbRefreshToken)) {
			isValid = false;
		}
		
		// 유효하지 않은 경우에는 재 로그인을 진행.
		// 이미 refreshToken이 만료가 되어버린 상황.
		if(!isValid) {
			result.put("message", "로그인이 필요합니다.");
			return new ResponseEntity<Map<String, Object>>(result, HttpStatus.FORBIDDEN);
		}
		
		// 유효한 경우에는 refreshToken과 accessToken을 재발급
		String newAccessToken = jwtUtil.createAccessToken(userId);
		String newRefreshToken = jwtUtil.createRefreshToken(userId);
		
		// RefreshToken은 HttpOnly Cookie로 발급
		Cookie cookie = new Cookie("refreshToken", newRefreshToken);
		cookie.setMaxAge(refreshTokenExpireTime);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		response.addCookie(cookie);

		// 새로 발급받은 refreshToken을 Database에 저장.
		AuthDto authDto = new AuthDto(userId, newRefreshToken);
		authService.updateRefreshToken(authDto);
		
		result.put("Authorization", newAccessToken);
		
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.CREATED);
	}
}
