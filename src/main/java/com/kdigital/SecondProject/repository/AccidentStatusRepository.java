package com.kdigital.SecondProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kdigital.SecondProject.entity.AccidentStatusEntity;

public interface AccidentStatusRepository extends JpaRepository<AccidentStatusEntity, String> {
	
	List<AccidentStatusEntity> findByPort_PortCode(String portCode);
}
