package com.kdigital.SecondProject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kdigital.SecondProject.service.AISService;

/**
 * AIS 테이블의 Service 테스트용
 * 	+ 항해 중 선박인 V7ER5, vNumber는 95

 * jUnitTest로 실행할 것
 * */

@SpringBootTest
public class AISTest {
	@Autowired
	private AISService service;
	
	/**
	 * 항해 번호로 ais 신호 추출 
	 * @param vNumber
	 * @return List<AISDTO>
	 * */
	@Test
	void testGetAllSignal() {
		service.selectAISAll((long) 63);
	}
}
