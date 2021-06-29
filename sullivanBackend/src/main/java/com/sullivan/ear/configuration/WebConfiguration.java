package com.sullivan.ear.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sullivan.ear.interceptor.JwtAuthInterceptor;


@Configuration
public class WebConfiguration implements WebMvcConfigurer {
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new JwtAuthInterceptor())
		.addPathPatterns("/sign/**")
		.addPathPatterns("/customer/**")
		.addPathPatterns("/user/**")
		.addPathPatterns("/management/**")
		.addPathPatterns("/reservation/**");
		//.excludePathPatterns("/api/auth/test", "/api/auth/kakao", "/api/auth/kakao/callback", "/scripts/**");
	}

}
