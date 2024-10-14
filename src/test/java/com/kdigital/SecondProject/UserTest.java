package com.kdigital.SecondProject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kdigital.SecondProject.service.UsersService;

@SpringBootTest
public class UserTest {
	@Autowired
	   private UsersService service;
	   
	   /**
	    * 회원 아이디로 특정 회원 검색
	    * */
	   @Test
	   void testSearchUser() {
	      service.existId("user001");
	   }

}
