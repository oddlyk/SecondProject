package com.kdigital.SecondProject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kdigital.SecondProject.service.AccidentStatusService;

/**
 * 사고 정보 DB의 Service 테스트용
 * 
 * jUnitTest로 실행할 것
 * */
@SpringBootTest
public class AccidentStatusServiceTest {
	@Autowired
	private AccidentStatusService service;
	
	/**
	 * 항구 코드로 사고 상태 조회
	 * */
	@Test
	void portAccidentInfo() {
		service.getAccidentStatusByPortCode("KRINC");
	}
}
