package com.kdigital.SecondProject.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kdigital.SecondProject.dto.UserDTO;
import com.kdigital.SecondProject.dto.VoyageDTO;
import com.kdigital.SecondProject.service.VoyageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {
	final VoyageService voyageService;
	
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
		// 검색을 통해 접근했다면...
		if(!shipInfo.equals("0")) {
			voyageDTO = voyageService.selectVoyageWithCallSign(shipInfo);
			log.info("(service) call sign으로 찾아온 항해 정보: {}",voyageDTO);
			if(voyageDTO!=null) {
				model.addAttribute("voyage", voyageDTO);
			}
		}
		return "main";
	}
}
