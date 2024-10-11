package com.kdigital.SecondProject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kdigital.SecondProject.service.VoyageService;

/**
 * 항해 테이블의 Service 테스트용
 *  + 항해 존재 선박 : C6TI4
 *  + 항해 중 선박 : V7ER5
 *  + 항해 없는 선박 : 2BOK5
 * jUnitTest로 실행할 것
 * */

@SpringBootTest
public class VoyageTest {
	@Autowired
	private VoyageService service;
	
	/**
	 * 선박의 콜사인으로 특정 항해 검색
	 * */
	@Test
	void testSearchVoyageAsCallSign() {
		service.selectVoyageWithCallSign("C6TI4");
	}
}
