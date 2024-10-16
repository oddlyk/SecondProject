package com.kdigital.SecondProject.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Security로 login 하는 DTO
@ToString
public class LoginUserDetails implements UserDetails {
	private static final long serialVersionUID = 1L;
	private String userId;
	private String userName;
	private String userPwd;
	private String email;
	private String roles;
	private Boolean enabled;
	
	// 생성자
	public LoginUserDetails(UserDTO userDTO) {
		this.userId = userDTO.getUserId();
		this.userPwd = userDTO.getUserPwd();
		this.userName = userDTO.getUserName();
		this.email = userDTO.getEmail();
	}
	
	// 사용자의 Role의 정보 반환
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collection = new ArrayList<>();
		collection.add(new GrantedAuthority() {
			private static final long serialVersionUID = 1L;

			@Override
			public String getAuthority() {
				return roles;
			}
		});
		
		return collection;
	}

	@Override
	public String getPassword() {
		return this.userPwd;
	}

	// Security가 설정한 메소드 (사용자의 아이디)
	@Override
	public String getUsername() {
		
		return this.userId;
	}

	// 사용자 정의 메소드(뷰단에서 사용할 사용자의 실명 이름)
	public String getUserName() {
		return this.userName;
	}
}
