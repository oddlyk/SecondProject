package com.kdigital.SecondProject;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kdigital.SecondProject.dto.PortInfoBDTO;
import com.kdigital.SecondProject.service.PortInfoBService;

import lombok.extern.slf4j.Slf4j;

/**
 * 항구 정보 B DB의 Service 테스트용
 * jUnitTest로 실행할 것
 * */

@SpringBootTest
@Slf4j
public class PortInfoBTest {
	@Autowired
	private PortInfoBService service;
	
	/**
	 * 항구 코드로 항구 정보 B 조회
	 * */
	@Test
	void getPortInfoB() {
		List<PortInfoBDTO> temp = service.getPortInfoByPortCode("KRINC");
		log.info("(test) 건네져온 PortInfoBDTO의 크기: {}",temp.size());
		log.info("(test) 건네져온 PortInfoBDTO의 0번째 데이터: {}",temp.get(0).toString());
	}
}
