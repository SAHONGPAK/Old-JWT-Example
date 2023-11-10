package com.example.jwt.util;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {
	
	@Value("jwt.key")
	private String jwtKey;
	
	@Value("${jwt.accesstoken.expiretime}")
	private Long accessTokenExpireTime;
	
	@Value("${jwt.refreshtoken.expiretime}")
	private Long refreshTokenExpireTime;
	
	public String createAccessToken(String userId) throws UnsupportedEncodingException {
	
		// 현재 시간을 저장한다.
		long currentTimeMillis = System.currentTimeMillis();
		
		// accessToken을 생성한다.
		JwtBuilder jwtAccessTokenBuilder = Jwts.builder();
		jwtAccessTokenBuilder.claim("userId", userId);
		jwtAccessTokenBuilder.setIssuedAt(new Date(currentTimeMillis));
		jwtAccessTokenBuilder.setExpiration(new Date(currentTimeMillis + accessTokenExpireTime*1000));
		jwtAccessTokenBuilder.signWith(SignatureAlgorithm.HS256, jwtKey.getBytes("UTF-8"));
	
		return jwtAccessTokenBuilder.compact();
	}
	
	
	public String createRefreshToken(String userId) throws UnsupportedEncodingException {
		
		// 현재 시간을 저장한다.
		long currentTimeMillis = System.currentTimeMillis();
		
		
		// refreshToken을 생성한다.
		JwtBuilder jwtRefreshTokenBuilder = Jwts.builder();
		jwtRefreshTokenBuilder.claim("userId", userId);
		jwtRefreshTokenBuilder.setIssuedAt(new Date(currentTimeMillis));
		jwtRefreshTokenBuilder.setExpiration(new Date(currentTimeMillis + refreshTokenExpireTime*1000));
		jwtRefreshTokenBuilder.signWith(SignatureAlgorithm.HS256, jwtKey.getBytes("UTF-8"));
	
		return jwtRefreshTokenBuilder.compact();
	}
	
	public String getUserId(String authorization) throws ParseException {
		// accessToken을 파싱한다.
		String[] chunks = authorization.split("\\.");
		Base64.Decoder decoder = Base64.getUrlDecoder();
		
		// payload에 저장된 userId를 획득한다.
		String payload = new String(decoder.decode(chunks[1]));
		JSONParser parser = new JSONParser();
		JSONObject obj = (JSONObject) parser.parse(payload);
		
		String userId = (String) obj.get("userId");
		
		return userId;
	}
	
	public boolean vaildCheck(String token) {
		
		// 해당 토큰을 확인하면서 예외가 발생하는 경우
		// 만료가 되었거나, 잘못된 토큰이다.
		try {
			Jwts.parser().setSigningKey(jwtKey.getBytes("UTF-8")).parseClaimsJws(token);
		}
		catch (Exception e) {
			// 예외가 발생한 경우 유효한 토큰이 아니다.
			return false;
		}
	
		// 예외가 발생하지 않았다면 유효한 토큰이다.
		return true;
	}
}
