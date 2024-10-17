package com.kdigital.SecondProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kdigital.SecondProject.entity.PortInfoAEntity;

public interface PortInfoARepository extends JpaRepository<PortInfoAEntity, Integer> {

	List<PortInfoAEntity> findByPort_PortCode(String portCode);
	
	// LocType으로 PortInfoAEntity 검색
	List<PortInfoAEntity> findByLocType(int locType);
	
	List<PortInfoAEntity> findByPort_PortCodeAndLocType(String portCode, int locType);
}
