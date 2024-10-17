package com.kdigital.SecondProject.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kdigital.SecondProject.dto.PortDTO;
import com.kdigital.SecondProject.dto.ShipDTO;
import com.kdigital.SecondProject.dto.VoyageDTO;
import com.kdigital.SecondProject.entity.PortEntity;
import com.kdigital.SecondProject.service.PortService;
import com.kdigital.SecondProject.service.ShipService;
import com.kdigital.SecondProject.service.VoyageService;

import lombok.extern.slf4j.Slf4j;

/**
 * 계산기 정보 화면으로 이동
 * */
@Controller
@Slf4j
public class CalcController {
	
	@Autowired
	private VoyageService voyageService;
	private ShipService shipService; 
	private PortService portService;
	
	
	/**
	 * 상단 메뉴바를 통한 계산 정보 화면 요청
	 * */
	
	@GetMapping("port/calcdetail")
	public String hearderPortD(Model model) {
		// 기본값 설정
		model.addAttribute("portName", "국내항");
		model.addAttribute("tonnage", 0);
				
		LocalDate today = LocalDate.now();
		model.addAttribute("importDate", today);
		model.addAttribute("exportDate", today);
			
		// 작업 시간과 대기 시간 설정
	    int defaultHour = 0;
	    int defaultMinute = 0;
	    
	    model.addAttribute("workingHour", defaultHour);
	    model.addAttribute("workingMinute", defaultMinute);
	    model.addAttribute("waitingHour", defaultHour);
	    model.addAttribute("waitingMinute", defaultMinute);
				
		model.addAttribute("shipName", "선박명");
		model.addAttribute("callSign", "(Call Sign)");
		model.addAttribute("result", 0);
		model.addAttribute("fee", "-");
		model.addAttribute("portion", "-");
		
		return "pages/calculator";
	}
	
	@GetMapping("calc/calcdetail")
	public String mainLink(@RequestParam("callSign") String callSign, Model model) {
		// call sign 기준 항해, 선박 정보 조회
		VoyageDTO voyage = voyageService.selectVoyageWithCallSign(callSign);
		ShipDTO ship = shipService.selectOneShip(callSign);
		
		// portCode로 항구 정보 조회
		PortEntity portEntity = voyage.getPort();
		String portCode = portEntity.getPortCode();
		PortDTO port = portService.selectPortByPortCode(portCode);
		
	
		
		// arrovalDate와 avgWaitingTime 더하기
		LocalDateTime arrivalDate = voyage.getArrivalDate();
		double avgWaitingTime = port.getAvgWaitingTime();
		
		long hours = (long) avgWaitingTime;		// 정수인 시간 부분
		long minutes = (long) ((avgWaitingTime - hours) * 60); 		// 소수점 시간 부분을 분으로 변환
		
		LocalDateTime exportDate = arrivalDate.plusHours(hours).plusMinutes(minutes);
		
		
		
		// 대기 시간과 작업 시간 변환
		// 1. 작업 시간
		double time = port.getAvgWorkingTime();
		int hour = (int) time;
		int minute = (int) ((time - hour) * 60);
		
		// 2. 대기 시간
		int hour1 = (int) avgWaitingTime;
		int minute1 = (int) ((avgWaitingTime - hour1) * 60);
		
		// 기본 값 설정
		model.addAttribute("portName", port.getPortName());
		model.addAttribute("tonnage", ship.getTonnage());
		model.addAttribute("importDate", voyage.getArrivalDate());
		model.addAttribute("exportDate", exportDate);
		model.addAttribute("workingHour", hour);
	    model.addAttribute("workingMinute", minute);
	    model.addAttribute("waitingHour", hour1);
	    model.addAttribute("waitingMinute", minute1);
		model.addAttribute("shipName", ship.getShipName());
		model.addAttribute("callSign", callSign);
		
		// exportDate를 수정할 수 없도록 설정
	    model.addAttribute("isExportDateDisabled", true);
		
		
		
		return "pages/calculator";
	}
	
}
