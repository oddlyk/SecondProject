package com.kdigital.SecondProject.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kdigital.SecondProject.dto.AISDTO;
import com.kdigital.SecondProject.dto.UserDTO;
import com.kdigital.SecondProject.dto.VoyageDTO;
import com.kdigital.SecondProject.service.AISService;
import com.kdigital.SecondProject.service.VoyageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {
	final VoyageService voyageService;
	final AISService aisService;
	
	/**
	 * 첫 화면 요청
	 * @return "main.html"
	 * */
	@GetMapping({"","/"})
	public String main(
			@AuthenticationPrincipal  UserDTO loginUser, //인증받은 사용자가 있다면 그 정보를 담아옴
			@RequestParam(name="ship", defaultValue="0") String shipInfo, //검색버튼 클릭 시
			Model model
			) {
		// 항해 정보 저장
		VoyageDTO voyageDTO = new VoyageDTO();
		log.info("(service) 데이터를 입력 받기 전의 항해 정보: {}",voyageDTO.toString());
		// 인증을 받은 사용자라면 그 이름 저장 
		if(loginUser!=null) {
			model.addAttribute("loginName", loginUser.getUserId());
		}
		
		// 검색을 통해 접근했다면 선박 검색
		if(!shipInfo.equals("0")) {
			VoyageDTO temp = voyageService.selectVoyageWithCallSign(shipInfo);
			log.info("(service) call sign으로 찾아온 항해 정보: {}",temp);
			if(temp!=null) voyageDTO = temp;
			
			temp = voyageService.selectVoyageWithMmsi(shipInfo);
			log.info("(service) MMSI로 찾아온 항해 정보: {}",temp);
			if(temp!=null) voyageDTO = temp;
			
			temp = voyageService.selectVoyageWithImo(shipInfo);
			log.info("(service) IMO로 찾아온 항해 정보: {}",temp);
			if(temp!=null) voyageDTO = temp;
			
			model.addAttribute("voyage", voyageDTO);
			
			double voyagePer = getVoyagePer(voyageDTO.getVNumber());
		}
		return "main";
	}

	private double getVoyagePer(Long vNumber) {
		//LocalDateTime de = voyageService.selectVoyageWithCallSign(null)
		LocalDateTime currentSignal = aisService.currentAISsignal(vNumber).getSignalDate();
		
		return 0;
	}
}
