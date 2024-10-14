package com.kdigital.SecondProject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.kdigital.SecondProject.service.FavoriteVoyageService;

@SpringBootTest
public class FavoriteVoyageTest {
	@Autowired
	   private FavoriteVoyageService service;
	
	/**
	 * 회원 아이디로 선호 항해 검색
	 * */
	@Test
	void searchFavVoyage() {
		service.favorite("V7ER5");
	}
}
