package com.kdigital.SecondProject;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kdigital.SecondProject.service.ShipService;

import lombok.extern.slf4j.Slf4j;

/**
 * 선박 DB의 Service 테스트용
 * 
 * jUnitTest로 실행할 것
 * */

@Slf4j
@SpringBootTest
public class ShipTest {
	@Autowired
	private ShipService service;
	
	/**
	 * 선박의 콜사인으로 특정 선박 검색
	 * */
	@Test
	void testSearchShip() {
		service.selectOneShip("2BOK5");
	}
}
