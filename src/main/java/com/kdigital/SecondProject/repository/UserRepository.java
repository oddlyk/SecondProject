package com.kdigital.SecondProject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kdigital.SecondProject.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {
	Optional<UserEntity> findByEmail(String email);
}
