package com.kdigital.SecondProject;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import com.kdigital.SecondProject.service.FavoriteVoyageService;

@SpringBootTest
public class FavoriteVoyageTest {
	@Autowired
	   private FavoriteVoyageService service;
	
	/**
	 * 선호 항해 등록 테스트
	 * */
	@Test
	@WithMockUser(username="user001", roles= {"USER"})
	void searchFavVoyage() {
		boolean result = service.favorite("V7ER5");
		System.out.println("Test result: " + result);
		assertTrue(result);
	}
}
