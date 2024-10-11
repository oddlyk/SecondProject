package com.kdigital.SecondProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kdigital.SecondProject.entity.VoyageEntity;

public interface VoyageRepository extends JpaRepository<VoyageEntity, Long> {
	
	// callSign으로 검색
	List<VoyageEntity> findByCallSign(String callSign);
}
