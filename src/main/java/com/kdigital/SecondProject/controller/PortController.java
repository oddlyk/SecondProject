package com.kdigital.SecondProject.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kdigital.SecondProject.dto.AccidentStatusDTO;
import com.kdigital.SecondProject.dto.PortDTO;
import com.kdigital.SecondProject.dto.PortInfoADTO;
import com.kdigital.SecondProject.dto.PortInfoBDTO;
import com.kdigital.SecondProject.service.AccidentStatusService;
import com.kdigital.SecondProject.service.PortInfoAService;
import com.kdigital.SecondProject.service.PortInfoBService;
import com.kdigital.SecondProject.service.PortService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 목적지 정보 화면으로 이동
 */
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
	public String headerPortDetail(@RequestParam(value = "port", defaultValue = "KRPUS") String portCode,
			Model model, HttpSession session) {
		log.info("항구 코드 : {}", portCode);
		
		// 항구 정보 가져오기
		PortDTO portDTO = portService.selectPortByPortCode(portCode);
		log.info("항구정보 : {}", portDTO);
		model.addAttribute("port", portDTO);
		
		// 전년도 동일 월의 사고 정보 가져오기
		List<AccidentStatusDTO> accidentStatusList = accidentStatusService.getAccidentStatusByPortCode(portCode);
		log.info("사고정보: {}", accidentStatusList);
		model.addAttribute("accidentStatus", accidentStatusList);
		
		// 인근 대기지, 터미널 위치 데이터 가져오기
		List<PortInfoADTO> portInfoAList = portInfoAService.getPortInfoByPortCode(portCode);
		
		// loc_type = 1 : 인근 대기지
		List<PortInfoADTO> waitingAreas = portInfoAList.stream()
				.filter(info -> info.getLocType() == 1)
				.collect(Collectors.toList());
		log.info("대기지 정보: {}", waitingAreas);
		model.addAttribute("waitingAreas", waitingAreas);
		
		// loc_type = 3 : 컨테이너 터미널
		List<PortInfoADTO> containerTerminals = portInfoAList.stream()
				.filter(info -> info.getLocType() == 3)
				.collect(Collectors.toList());
		log.info("터미널 정보: {}", containerTerminals);
		model.addAttribute("containerTerminals", containerTerminals);
		
		// 혼잡 주의 지역 데이터 가져오기
		List<PortInfoBDTO> portInfoBList = portInfoBService.getPortInfoByPortCode(portCode);
		log.info("혼잡주의지역 정보 : {}", portInfoBList);
		model.addAttribute("congestionAreas", portInfoBList);
		
		//기존 세션 확인 및 값 전달
		if(session.getAttributeNames().hasMoreElements()) {
		   model.addAttribute("session_port",(String) session.getAttribute("session_port"));
		   model.addAttribute("session_callsign",(String) session.getAttribute("session_callSign"));
		}
		
		return "pages/destination";
	}
	
	@GetMapping("/port/changePort")
	@ResponseBody
	public Map<String, Object> changePort(@RequestParam(value = "port") String portCode) {
	    Map<String, Object> response = new HashMap<>();
	    
	    // 항구 정보 가져오기
	    PortDTO portDTO = portService.selectPortByPortCode(portCode);
	    log.info("항구정보 : {}", portDTO);
	    response.put("port", portDTO);
	    
	    // 전년도 동일 월의 사고 정보 가져오기
	    List<AccidentStatusDTO> accidentStatusList = accidentStatusService.getAccidentStatusByPortCode(portCode);
	    log.info("사고정보: {}", accidentStatusList);
	    response.put("accidentStatus", accidentStatusList);
	    
	    // 인근 대기지, 터미널 위치 데이터 가져오기
	    List<PortInfoADTO> portInfoAList = portInfoAService.getPortInfoByPortCode(portCode);
	    
	    // loc_type = 1 : 인근 대기지
	    List<PortInfoADTO> waitingAreas = portInfoAList.stream()
	            .filter(info -> info.getLocType() == 1)
	            .collect(Collectors.toList());
	    log.info("대기지 정보: {}", waitingAreas);
	    response.put("waitingAreas", waitingAreas);
	    
	    // loc_type = 3 : 컨테이너 터미널
	    List<PortInfoADTO> containerTerminals = portInfoAList.stream()
	            .filter(info -> info.getLocType() == 3)
	            .collect(Collectors.toList());
	    log.info("터미널 정보: {}", containerTerminals);
	    response.put("containerTerminals", containerTerminals);
	    
	    // 혼잡 주의 지역 데이터 가져오기
	    List<PortInfoBDTO> portInfoBList = portInfoBService.getPortInfoByPortCode(portCode);
	    log.info("혼잡주의지역 정보 : {}", portInfoBList);
	    response.put("congestionAreas", portInfoBList);
	    
	    // JSON 형태로 모든 데이터 반환
	    return response;
	}
}

