package com.example.jwt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//ElementType.METHOD: Method에 붙여줄 수 있다.
@Target({ElementType.METHOD, ElementType.TYPE})

//Annotation이 언제까지 유지될지.
//RetentionPolicy.RUNTIME: Runtime 동안 유지된다.
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthRequired {

}
