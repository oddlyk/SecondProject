package com.kdigital.SecondProject.controller;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kdigital.SecondProject.dto.AISDTO;
import com.kdigital.SecondProject.dto.LoginUserDetails;
import com.kdigital.SecondProject.dto.VoyageDTO;
import com.kdigital.SecondProject.service.AISService;
import com.kdigital.SecondProject.service.VoyageService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 항해 정보 세부 페이지로 연결
 * */

@Controller
@RequiredArgsConstructor
@Slf4j
public class VoyageController {
	final VoyageService voyageService;
	final AISService aisService;
	

	/**
	 * 콜사인을 포함하여 항해 정보 세부페이지로
	 * @callSign
	 * @return  shipInfo.html
	 * */
	@GetMapping({"voyage/voyagedetail"})
	public String voyageDetail(
			@AuthenticationPrincipal  LoginUserDetails loginUser, //인증받은 사용자가 있다면 그 정보를 담아옴
			@RequestParam(name="callSign", defaultValue="-1") String callSign, //검색버튼 클릭 시
			HttpSession session,
			Model model
			) {
		//선박명, 콜사인, 출발일시, 항해 진행도, 출발항 , 도착항, 도착일시
		VoyageDTO dto = voyageService.selectVoyageWithCallSign(callSign);
		log.info("받아온 항해 정보: {}",dto.toString());
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		log.info("받아온 항해 정보: {}",dto.toString());
		model.addAttribute("voyage", dto);

		// 현속도, 이동방향, 현좌표 
		AISDTO nowdto = aisService.currentAISsignal(dto.getVNumber());
		double nowSpeed = 0;
		double nowCor = 0;
		String nowLoc = "";
		String voyagePer = "0.0";
		long leftDate = Duration.between(dto.getArrivalDate(),LocalDateTime.now()).toDays();
		if(nowdto!=null) {
			nowLoc="{ \"lat\": "+nowdto.getLatitude()+", \"lng\": "+nowdto.getLongitude()+" }";
			nowSpeed = nowdto.getSpeed();
			nowCor = nowdto.getDirection();
			// 항해 진행률 생성 및 전달
			voyagePer = getVoyagePer(dto.getVNumber());
			
			
			//남은 일시 계산
			leftDate = Duration.between(dto.getArrivalDate(),nowdto.getSignalDate()).toDays();
		}
		log.info("현속도: {}",nowSpeed);
		log.info("이동방향: {}",nowCor);
		log.info("현위치: {}",nowLoc);
		model.addAttribute("nowSpeed", nowSpeed);
		model.addAttribute("nowCor", nowCor);
		model.addAttribute("nowLoc", nowLoc);
		model.addAttribute("voyagePer", voyagePer);
		model.addAttribute("leftDate", leftDate);
		
		/*
		 * 
		//과거 좌표들 latitude=29.84079, longitude=122.20612
		List<AISDTO> aisList = aisService.selectAISAll(dto.getVNumber());
		String formerLoc = "[";
		if(aisList!=null) {
			log.info("신호 개수: {}",aisList.size());
			for(int i=0;i<aisList.size();++i) {
				formerLoc+="{ \"lat\": "+aisList.get(i).getLatitude()+", \"lng\": "+aisList.get(i).getLongitude()+" }";
				if(i<aisList.size()-1) {
					formerLoc+=" , ";
				}
			}
		}
		formerLoc+="]";
		//System.out.println(formerLoc);
		model.addAttribute("formerLoc", formerLoc);
		 * */
		//기존 세션 확인 및 값 전달
		if(session.getAttributeNames().hasMoreElements()) {
			model.addAttribute("session_port",(String) session.getAttribute("session_port"));
			model.addAttribute("session_callsign",(String) session.getAttribute("session_callSign"));
		}
		return "/pages/shipInfo";
	}
	
	
	private String getVoyagePer(Long vNumber) {
		LocalDateTime arrivalDate = voyageService.selectOne(vNumber).getArrivalDate();
		LocalDateTime departureDate = voyageService.selectOne(vNumber).getDepartureDate();
		LocalDateTime currentSignal = null;
		AISDTO aisDTO = aisService.currentAISsignal(vNumber);
		if(aisDTO==null) {
			log.info("항해 시작 전");
			return String.format("%.2f", 0.0);
		}
		currentSignal = aisDTO.getSignalDate(); // aisDTO가 null이 아닐 때만 호출
		long totalVoyageMinutes = Duration.between(departureDate, arrivalDate).toMinutes();
		long untilTodayMinutes = Duration.between(currentSignal, arrivalDate).toMinutes();
		//현재 항해일 수 / 총 항해일 수 * 100 = 항해 진행률
		double voyageProgress = ((double) untilTodayMinutes / totalVoyageMinutes) * 100;
		if(voyageProgress==0) {
			voyageProgress=100;
		}
		log.info("현재 항해에 대한 항해 진행률: {}",voyageProgress);
		return String.format("%.2f", voyageProgress);
	}
	
}
