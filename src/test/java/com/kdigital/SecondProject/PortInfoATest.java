package com.kdigital.SecondProject;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kdigital.SecondProject.dto.PortInfoADTO;
import com.kdigital.SecondProject.service.PortInfoAService;

import lombok.extern.slf4j.Slf4j;

/**
 * 항구 정보 A DB의 Service 테스트용
 * jUnitTest로 실행할 것
 * */

@SpringBootTest
@Slf4j
public class PortInfoATest {
	@Autowired
	private PortInfoAService service;
	
	/**
	 * 항구 코드로 항구 정보 A 조회
	 * */
	@Test
	void getPortInfoA() {
		List<PortInfoADTO> temp = service.getPortInfoByPortCode("KRINC");
		log.info("(test) 건네져온 PortInfoADTO의 크기: {}",temp.size());
		log.info("(test) 건네져온 PortInfoADTO의 0번째 데이터: {}",temp.get(0).toString());
	}
}
