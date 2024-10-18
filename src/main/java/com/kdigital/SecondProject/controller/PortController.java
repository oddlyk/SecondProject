package com.kdigital.SecondProject.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String headerPortDetail(@RequestParam(value = "port", defaultValue = "KRPUS") String portCode,
			@RequestParam(name="search_ship", defaultValue="-1") String search_ship,
			Model model) {
		log.info("항구 코드 : {}", portCode);
		log.info("검색 선박 코드 : {}", search_ship);
		model.addAttribute("search_ship",search_ship); //선박명 부분
		
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
	
	/**
     * 항구 정보 동적으로 가져오기 (셀렉트박스 전환 시)
     */
    @GetMapping("/api/ports/{portCode}")
    public PortDTO getPortByCode(@PathVariable String portCode) {
        log.info("포트 코드: {}", portCode);
        return portService.selectPortByPortCode(portCode);
    }

    /**
     * 전년도 동일 월의 사고 정보 가져오기 (셀렉트박스 전환 시)
     */
    @GetMapping("/api/accident-status/{portCode}")
    public List<AccidentStatusDTO> getAccidentStatusByPortCode(@PathVariable String portCode) {
        log.info("포트 코드 : {}, 사고 정보 요청", portCode);
        return accidentStatusService.getAccidentStatusByPortCode(portCode);
    }

    /**
     * 인근 대기지 정보 동적으로 가져오기 (셀렉트박스 전환 시)
     */
    @GetMapping("/api/waiting-areas/{portCode}")
    public List<PortInfoADTO> getWaitingAreasByPortCode(@PathVariable String portCode) {
        log.info("포트 코드 : {}, 인근 대기지 정보 요청", portCode);
        return portInfoAService.getPortInfoByPortCodeAndLocType(portCode, 1); // loc_type이 1이면 인근 대기지
    }

    /**
     * 컨테이너 터미널 정보 동적으로 가져오기 (셀렉트박스 전환 시)
     */
    @GetMapping("/api/container-terminals/{portCode}")
    public List<PortInfoADTO> getContainerTerminalsByPortCode(@PathVariable String portCode) {
        log.info("포트 코드 : {}, 컨테이너 터미널 정보 요청", portCode);
        return portInfoAService.getPortInfoByPortCodeAndLocType(portCode, 3); // loc_type이 3이면 컨테이너 터미널
    }

    /**
     * 혼잡 주의 지역 정보 동적으로 가져오기 (셀렉트박스 전환 시)
     */
    @GetMapping("/api/congestion-areas/{portCode}")
    public List<PortInfoBDTO> getCongestionAreasByPortCode(@PathVariable String portCode) {
        log.info("포트 코드 : {}, 혼잡 주의 지역 정보 요청", portCode);
        return portInfoBService.getPortInfoByPortCode(portCode);
    }
}
	
//	@GetMapping("/api/waiting-areas")
//	public List<PortInfoADTO> getWaitingAreas() {
//	    // loc_type이 1인 인근 대기지 데이터를 가져옵니다.
//	    return portInfoAService.getPortInfoByLocType(1);
//	}
//	
//	@GetMapping("/api/ports/{portCode}")
//	public PortDTO getPortByCode(@PathVariable String portCode) {
//	    log.info("포트 코드: {}", portCode);
//	    return portService.selectPortByPortCode(portCode);
//	}
//	
//	@GetMapping("/api/accident-status/{portCode}")
//	public List<AccidentStatusDTO> getAccidentStatusByPortCode(@PathVariable String portCode) {
//	    return accidentStatusService.getAccidentStatusByPortCode(portCode);
//	}
//}
