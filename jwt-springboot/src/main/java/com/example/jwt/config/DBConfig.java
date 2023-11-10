package com.example.jwt.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = {"com.example.jwt.model.dao"}) // DAO 경로를 지정.
public class DBConfig {

}