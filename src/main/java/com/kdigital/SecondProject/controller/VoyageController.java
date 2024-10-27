package com.kdigital.SecondProject.controller;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
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
		dto.setArrivalDate(dto.getArrivalDate().plusYears(2));
		dto.setDepartureDate(dto.getDepartureDate().plusYears(2));
		
		log.info("받아온 항해 정보: {}",dto.toString());
		model.addAttribute("voyage", dto);

		// 현속도, 이동방향, 현좌표 
		AISDTO nowdto = aisService.currentAISsignal(dto.getVNumber());
		double nowSpeed = 0;
		double nowCor = 0;
		String nowLoc = "";
		String voyagePer = "0.0";
		String leftDateS = "";
		
		LocalDateTime today = LocalDate.now().atStartOfDay();
		LocalDateTime arrivalDate = dto.getArrivalDate().toLocalDate().atStartOfDay();

		long leftDate = ChronoUnit.DAYS.between(today, arrivalDate); //long leftDate = Duration.between(dto.getArrivalDate(),LocalDateTime.now()).toDays();
		leftDateS = "-"+Long.toString(leftDate);
		if (leftDate > 0) {
		    System.out.println("디데이: D-" + leftDate);
		    leftDateS = "-"+Long.toString(leftDate);
		} else if (leftDate == 0) {
		    System.out.println("디데이: 오늘 도착 (D-Day)");
		    leftDateS = "-DAY";
		} else {
		    System.out.println("디데이: D+" + Math.abs(leftDate) + " (이미 종료된 항해)");
		    leftDateS = "-0";
		}
		
		if(nowdto!=null) {
			nowLoc="{ \"lat\": "+nowdto.getLatitude()+", \"lng\": "+nowdto.getLongitude()+" }";
			nowSpeed = nowdto.getSpeed();
			nowCor = nowdto.getDirection();
			// 항해 진행률 생성 및 전달
			voyagePer = getVoyagePer(dto.getVNumber());
			//남은 일시 계산
			LocalDateTime signalDate = nowdto.getSignalDate().plusYears(2).toLocalDate().atStartOfDay();
			// 디데이 계산
			if (signalDate.isAfter(arrivalDate)) {
			    System.out.println("도착일이 초과되었습니다.");
			    leftDate = ChronoUnit.DAYS.between(arrivalDate, signalDate); 
			    leftDateS = "+"+Long.toString(Math.abs(leftDate));
			}
			
//			leftDate = java.time.Period.between(
//				    nowdto.getSignalDate().toLocalDate().plusYears(2),  // 현재 시그널 날짜를 LocalDate로 변환하고 2년 더함
//				    dto.getArrivalDate().toLocalDate()                  // 도착 날짜를 LocalDate로 변환
//				).getDays();  // 남은 일수를 계산 //Duration.between(nowdto.getSignalDate().plusYears(2),dto.getArrivalDate()).toDays();
//			System.out.println("1차 계산된 남은 일수: "+leftDate);
//			if (Double.parseDouble(voyagePer) >= 100) {
//		        // 항해가 완료된 경우
//		        leftDateS = "-0";
//		        System.out.println("100% 달성 항해 -0일");
//		    } else {
//		        // 항해 진행 중인 경우
//		        if (leftDate > 0) {
//		            leftDateS = "-" + leftDate; // 남은 일수 표시
//		        } else {
//		            leftDateS = "+" + Math.abs(leftDate); // 초과 일수 표시
//		        }
//		        System.out.println("남은 일수 또는 초과 일수: " + leftDateS);
//		    }
//			leftDateS = Long.toString(leftDate);
//			System.out.println(nowdto.getSignalDate());
//			if(leftDate>0) {
//				if(Double.parseDouble(voyagePer)>=100) {
//					System.out.println("100% 달성 항해 -0일");
//					leftDateS = "-0";
//				}else {
//					leftDateS = "+"+leftDateS;
//					System.out.println("남은 일수: "+leftDateS);
//				}
//			}
//			System.out.println("남은 일수: "+leftDateS);
		}
		log.info("현속도: {}",nowSpeed);
		log.info("이동방향: {}",nowCor);
		log.info("현위치: {}",nowLoc);
		model.addAttribute("nowSpeed", nowSpeed);
		model.addAttribute("nowCor", nowCor);
		model.addAttribute("nowLoc", nowLoc);
		model.addAttribute("voyagePer", voyagePer);
		model.addAttribute("leftDate", leftDateS);
		
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
		VoyageDTO voyage = voyageService.selectOne(vNumber);
		LocalDateTime arrivalDate = voyage.getArrivalDate();
		LocalDateTime departureDate = voyage.getDepartureDate();
		AISDTO aisDTO = aisService.currentAISsignal(vNumber);
		if(aisDTO==null) {
			log.info("항해 시작 전");
			return String.format("%.2f", 0.0);
		}
		LocalDateTime currentSignal = aisDTO.getSignalDate(); // aisDTO가 null이 아닐 때만 호출
		// 전체 항해 시간(출발일 ~ 도착일)을 분 단위로 계산
		long totalVoyageMinutes = Duration.between(departureDate, arrivalDate).toMinutes();
		// 현재까지의 항해 시간(출발일 ~ 현재 위치 시점)을 분 단위로 계산
		long untilTodayMinutes = Duration.between(departureDate, currentSignal).toMinutes(); 
		//현재 항해일 수 / 총 항해일 수 * 100 = 항해 진행률
		double voyageProgress = ((double) untilTodayMinutes / totalVoyageMinutes) * 100;
		if(voyageProgress==0) {
			voyageProgress=100;
		}
		log.info("현재 항해에 대한 항해 진행률: {}",voyageProgress);
		return String.format("%.2f", voyageProgress);
	}
	
}
