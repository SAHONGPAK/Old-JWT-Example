package com.example.jwt.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.jwt.annotation.AuthRequired;
import com.example.jwt.util.JWTUtil;

@Component
public class AuthInterceptor implements HandlerInterceptor{
	private final String ACCESSTOKEN_HEADER = "Authorization";
	
	private final JWTUtil jwtUtil;
	
	private AuthInterceptor(JWTUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// handler가 메소드에서 호출된 것인지 확인.
		// 그렇지 않다면 모두 패스.
		if(!(handler instanceof HandlerMethod)) {
			return true;
		}
		
		
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		
		// 호출된 메소드의 Annotation이 AuthRequired인지 확인.
		if(
				handlerMethod.getMethodAnnotation(AuthRequired.class) != null ||
				handlerMethod.getBeanType().getAnnotation(AuthRequired.class) != null
		) {
			
			// accessToken과 refreshToken을 확인.
			String accessToken = request.getHeader(ACCESSTOKEN_HEADER);
			
			if(accessToken != null) {
				if(jwtUtil.vaildCheck(accessToken)) {
					return true;
				}
			}
			
			// 권한이 없음을 클라이언트에 전송해준다.
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			return false;
		}
		
		// AuthRequired가 붙지 않은 메소드인 경우 모두 통과
		return true;
	}
}
