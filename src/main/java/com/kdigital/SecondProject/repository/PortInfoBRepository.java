package com.kdigital.SecondProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kdigital.SecondProject.entity.PortInfoBEntity;

public interface PortInfoBRepository extends JpaRepository<PortInfoBEntity, Integer> {

	// 특정 항구 코드에 따른 PortInfoB 검색
    List<PortInfoBEntity> findByPort_PortCode(String portCode);
    
    // 특정 locType에 따른 PortInfoB 검색
    List<PortInfoBEntity> findByLocType(int locType);
}
