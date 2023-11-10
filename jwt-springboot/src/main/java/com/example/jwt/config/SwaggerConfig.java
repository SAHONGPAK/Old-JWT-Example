package com.example.jwt.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.info.Contact;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
	// Swagger-UI 3.0
	// http://localhost:8080/swagger-ui/index.html
	
	private static final String VERSION = "V1";
	private static final String TITLE = "JWT Example API " + VERSION;
	
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.OAS_30)
				.consumes(getConsumeContentTypes())
				.produces(getProduceContentTypes())
				.apiInfo(apiInfo())
					.groupName(VERSION)
					.select()
					.build()
					.useDefaultResponseMessages(false);
						
	}
	
	private Set<String> getConsumeContentTypes() {
		Set<String> consumes = new HashSet<>();
		consumes.add("application/json;charset=UTF-8");
		consumes.add("application/x-www-form-urlencoded");
		
		return consumes;
	}
	
	private Set<String> getProduceContentTypes() {
		Set<String> produces = new HashSet<>();
		produces.add("application/json;charset=UTF-8");
		
		return produces;
	}
	
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title(TITLE)
				.description("<h3>JWT Example API Reference for Developers</h3>Swagger를 이용한 Board API<br><img src=\"/assets/img/ssafy_logo.png\" width=\"150\">") 
				.license("MIT License")
				.licenseUrl("https://github.com/SAHONGPAK")
				.version("1.0").build();
	}
}
