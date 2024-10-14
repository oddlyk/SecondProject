package com.kdigital.SecondProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kdigital.SecondProject.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {
	//아이디로 검색
	List<UserEntity> findByUserId(String userId);
}
