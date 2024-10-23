package com.kdigital.SecondProject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.kdigital.SecondProject.handler.CustomSuccessHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final CustomSuccessHandler handler;
	
	@Bean //Spring에서 생성 주기를 관리하는 Bean 객체임을 명시
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		// +) POST 요청 시 CSRF(웹사이트 보안 공격 중 하나(Cross-Site Request Forgery)) 공격을 받을 수도 있음.
		//		따라서 본래 이를 보호하기 위해 key값을 가지고 있으나, 개발 시 번거로움이 있기에 일시적으로 막아둘 수 있음
		http.csrf((auth)->auth.disable())
			.authorizeHttpRequests((authz) -> authz
				.requestMatchers("/script/**", "/css/**", "/images/**").permitAll()  // 정적 리소스에 대한 접근 허용
	            .requestMatchers("/","/user/login",  "/user/join","/port/portdetail","/port/calcdetail","/user/idCheck").permitAll() // 로그인, 회원가입, 리소스 접근 허용
	            .requestMatchers("/user/idCheck","/user/emailCheck","/port/changePort","/aboutUs").permitAll()
	            .anyRequest().authenticated()  // 그 외의 요청은 인증 필요
	        )
	        .formLogin((form) -> form
	            .loginPage("/user/login")  // 커스텀 로그인 페이지 경로
	            .successHandler(handler) // 로그인에 성공했을 때 처리할 핸들러, defaultSuccessUrl를 빼고 사용해야 함.
	            .usernameParameter("id")
				.passwordParameter("pwd")
				.loginProcessingUrl("/user/loginProc")
	            //.defaultSuccessUrl("/", true)  // 로그인 성공 시 리다이렉트 경로
	            .permitAll()
	        )
	        .logout((logout) -> logout
	            .logoutUrl("/user/logout")
	            .logoutSuccessUrl("/")
	            .invalidateHttpSession(true) // 세션 무효화
	            .permitAll()
	        )
	        .headers(headers -> headers.frameOptions().disable());
	        
		return http.build();
	}
			
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // BCryptPasswordEncoder 빈 생성
    }
}
