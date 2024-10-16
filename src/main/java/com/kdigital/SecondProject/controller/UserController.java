package com.kdigital.SecondProject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kdigital.SecondProject.dto.UserDTO;
import com.kdigital.SecondProject.service.UsersService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class UserController {

	final UsersService userService;
	/*		
	@GetMapping("/user/login")
	public String loginPage() {
		System.out.println("로그인 화면 요청");
		return "pages/login";
	}
	 */

	//	@PostMapping("/user/loginProc")
	//	public String login(@RequestParam(name="id") String userId,
	//			@RequestParam(name="pwd") String userPwd,
	//			Model model) {
	//		System.out.println("knlkjnlnj"+ userId);
	//		if (userService.login(userId, userPwd)) return "/";
	//		else {
	//			model.addAttribute("errorMessage", "아이디 또는 비밀번호가 잘못되었습니다.");
	//			return "pages/login";
	//		}
	//		return "";

	/**
	 * 로그인 실패 시 처리 화면
	 * @param error
	 * @param errMessage
	 * @param model
	 * @return
	 */
	@GetMapping("/login")
	public String login(
			@RequestParam(value="error", required=false) String error 
			, @RequestParam(value="errMessage", required=false) String errMessage 
			, Model model
			) {

		System.out.println(error);
		System.out.println(errMessage);

		model.addAttribute("error", error);
		model.addAttribute("errMessage", errMessage);

		return "pages/login";
	}
}
