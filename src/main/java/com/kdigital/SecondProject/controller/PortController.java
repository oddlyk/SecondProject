package com.kdigital.SecondProject.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kdigital.SecondProject.dto.AccidentStatusDTO;
import com.kdigital.SecondProject.dto.PortDTO;
import com.kdigital.SecondProject.dto.PortInfoADTO;
import com.kdigital.SecondProject.dto.PortInfoBDTO;
import com.kdigital.SecondProject.service.AccidentStatusService;
import com.kdigital.SecondProject.service.PortInfoAService;
import com.kdigital.SecondProject.service.PortInfoBService;
import com.kdigital.SecondProject.service.PortService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 목적지 정보 화면으로 이동
 * */
@Controller
@RequiredArgsConstructor
@Slf4j
public class PortController {
	
	final PortService portService;
	final AccidentStatusService accidentStatusService;
	final PortInfoAService portInfoAService;
	final PortInfoBService portInfoBService;
	
	
	/**
	 * 상단 메뉴바를 통한 목적지 정보 화면 요청
	 * */
	
	@GetMapping("/port/portdetail")
	public String headerPortDetail(@RequestParam(value = "port", defaultValue = "KRPUS") String portCode, Model model) {
		log.info("항구 코드 : {}", portCode);
		
		// 항구 정보 가져오기
		PortDTO portDTO = portService.selectPortByPortCode(portCode);
		model.addAttribute("port", portDTO);
		
		// 전년도 동일 월의 사고 정보 가져오기
		List<AccidentStatusDTO> accidentStatusList = accidentStatusService.getAccidentStatusByPortCode(portCode);
		model.addAttribute("accidentStatus", accidentStatusList);
		
		// 인근 대기지, 터미널 위치 데이터 가져오기
		List<PortInfoADTO> portInfoAList = portInfoAService.getPortInfoByPortCode(portCode);
		
		// loc_type = 1 : 인근 대기지
		List<PortInfoADTO> waitingAreas = portInfoAList.stream()
				.filter(info -> info.getLocType() == 1)
				.collect(Collectors.toList());
		model.addAttribute("waitingAreas", waitingAreas);
		
		// loc_type = 3 : 컨테이너 터미널
		List<PortInfoADTO> containerTerminals = portInfoAList.stream()
				.filter(info -> info.getLocType() == 3)
				.collect(Collectors.toList());
		model.addAttribute("containerTerminals", containerTerminals);
		
		// 혼잡 주의 지역 데이터 가져오기
		List<PortInfoBDTO> portInfoBList = portInfoBService.getPortInfoByPortCode(portCode);
		model.addAttribute("congestionAreas", portInfoBList);
		
		return "pages/destination";
	}
	
	@GetMapping("/api/waiting-areas")
	public List<PortInfoADTO> getWaitingAreas() {
	    // loc_type이 1인 인근 대기지 데이터를 가져옵니다.
	    return portInfoAService.getPortInfoByLocType(1);
	}
}
