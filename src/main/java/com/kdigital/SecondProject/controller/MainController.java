package com.kdigital.SecondProject.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.kdigital.SecondProject.dto.UserDTO;

@Controller
public class MainController {
	/**
	 * 첫 화면 요청
	 * @return "main.html"
	 * */
	@GetMapping({"","/"})
	public String main(
			@AuthenticationPrincipal  UserDTO loginUser, //인증받은 사용자가 있다면 그 정보를 담아옴
			Model model
			) {
		// 인증을 받은 사용자
		if(loginUser!=null) {
			model.addAttribute("loginName", loginUser.getUserId());
		}
		return "main";
	}
}
