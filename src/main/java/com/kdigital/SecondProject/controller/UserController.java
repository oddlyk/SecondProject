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
	
	@GetMapping("/login")
	public String loginPage() {
		return "pages/login";
	}
	
	@PostMapping("/login")
	public String login(@RequestParam(name="id") String userId,
			@RequestParam(name="pwd") String userPwd,
			Model model) {
		if (userService.login(userId, userPwd)) return "/";
		else {
			model.addAttribute("errorMessage", "아이디 또는 비밀번호가 잘못되었습니다.");
			return "pages/login";
		}
	}

}
