package com.kdigital.SecondProject.dto;

import com.kdigital.SecondProject.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder

public class UserDTO {
	private String userId;
	private String userPwd;
	private String userName;
	private String email;
	private String phone;
	private int userType;
	
	// Entity를 받아서 ----> DTO로 반환 
	public static UserDTO toDTO(UserEntity userEntity) {
		return UserDTO.builder()
				.userId(userEntity.getUserId())
				.userPwd(userEntity.getUserPwd())
				.userName(userEntity.getUserName())
				.email(userEntity.getEmail())
				.phone(userEntity.getPhone())
				.userType(userEntity.getUserType())
				.build();
		}
}
