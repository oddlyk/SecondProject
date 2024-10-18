package com.kdigital.SecondProject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kdigital.SecondProject.dto.UserDTO;
import com.kdigital.SecondProject.service.UsersService;

@SpringBootTest
public class UserTest {
	@Autowired
	   private UsersService service;
	   
	   /**
	    * 회원 아이디로 특정 회원 검색
	    * */
	   // @Test
	   void testSearchUser() {
	      service.existId("user001");
	   }
	   
	   /**
	    * 회원 가입
	    * */
	   @Test
	   void joinUser() {
		   service.join(new UserDTO("user005","password5","사용자5","user5@naver.com","010-0000-0000",1));
	   }
}
