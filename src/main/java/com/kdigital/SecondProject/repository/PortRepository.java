package com.kdigital.SecondProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kdigital.SecondProject.entity.PortEntity;

public interface PortRepository extends JpaRepository<PortEntity, String> {
	// 아래에 쓰여지는 함수명은 JPA의 규칙을 따른 것
	
		// portCode로 검색
		List<PortEntity> findByPortCode(String portCode);  // PortEntity의 portcode를 기반으로 검색
	// 외래키 참조 형을 사용하지 않으면 findBy변수명 으로 함수명을 생성 : findByportCode
	// 외래키 참조 형을 사용했다면 findBy변수명(entity변수)_참조한entity의 찾고자 하는 컬럼명 으로 함수명 생성 : findByportCode
}
