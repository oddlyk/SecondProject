package com.kdigital.SecondProject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
	@Bean //Spring에서 생성 주기를 관리하는 Bean 객체임을 명시
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		// +) POST 요청 시 CSRF(웹사이트 보안 공격 중 하나(Cross-Site Request Forgery)) 공격을 받을 수도 있음.
		//		따라서 본래 이를 보호하기 위해 key값을 가지고 있으나, 개발 시 번거로움이 있기에 일시적으로 막아둘 수 있음
		http.csrf((auth)->auth.disable());
		return http.build();
	}
			
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // BCryptPasswordEncoder 빈 생성
    }
}
