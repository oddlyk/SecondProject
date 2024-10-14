package com.kdigital.SecondProject.entity;

import com.kdigital.SecondProject.dto.UserDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

@Entity
@Table(name="users")
public class UserEntity {
	@Id
	@Column(name="user_id")
	private String userId;
	
	@Column(name="user_pwd", nullable = false)
	private String userPwd;
	
	@Column(name="user_name", nullable = false)
	private String userName;
	
	@Column(name="email", nullable = false)
	private String email;
	
	@Column(name="phone", nullable = false)
	private String phone;
	
	@Column(name="user_type")
	private int userType;
	
	public static UserEntity toEntity(UserDTO userDTO) {
		return UserEntity.builder()
				.userId(userDTO.getUserId())
				.userPwd(userDTO.getUserPwd())
				.userName(userDTO.getUserName())
				.email(userDTO.getEmail())
				.phone(userDTO.getPhone())
				.userType(userDTO.getUserType())
				.build();
		}
}
