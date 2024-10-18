package com.kdigital.SecondProject.controller;

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
			Model model
			) {
		//선박명, 콜사인, 출발일시, 항해 진행도, 출발항 , 도착항, 도착일시
		VoyageDTO dto = voyageService.selectVoyageWithCallSign(callSign);
		log.info("받아온 항해 정보: {}",dto.toString());
		model.addAttribute("voyage", dto);

		// 현좌표 
		AISDTO nowdto = aisService.currentAISsignal(dto.getVNumber());
		String nowLoc = "";
		if(nowdto!=null) {
			nowLoc="{ \"lat\": "+nowdto.getLatitude()+", \"lng\": "+nowdto.getLongitude()+" }";
		}
		log.info("현위치: {}",nowLoc);
		model.addAttribute("nowLoc", nowLoc);
		
		
		//과거 좌표들 latitude=29.84079, longitude=122.20612
		List<AISDTO> aisList = aisService.selectAISAll(dto.getVNumber());
		log.info("신호 개수: {}",aisList.size());
		String formerLoc = "[";
		for(int i=0;i<aisList.size();++i) {
			formerLoc+="{ \"lat\": "+aisList.get(i).getLatitude()+", \"lng\": "+aisList.get(i).getLongitude()+" }";
			if(i<aisList.size()-1) {
				formerLoc+=" , ";
			}
		}
		formerLoc+="]";
		//System.out.println(formerLoc);
		model.addAttribute("formerLoc", formerLoc);
		
		//남은 일시 계산
		//항해 진행률
		
		
		return "/pages/shipInfo";
	}
	
	
}
