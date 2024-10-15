package com.kdigital.SecondProject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kdigital.SecondProject.service.PortService;

/**
 * 항구 정보 DB의 Service 테스트용
 * jUnitTest로 실행할 것
 * */

@SpringBootTest
public class PortTest {
	@Autowired
	private PortService service;
	
	/**
	 * 모든 항구 정보 조회
	 * */
	//@Test
	void getAllPort() {
		service.getAllPorts();
	}
	
	
	/**
	 * 항구 코드로 항구 정보 조회
	 * */
	@Test
	void searchPort() {
		service.selectPortByPortCode("KRINC");
	}
}
