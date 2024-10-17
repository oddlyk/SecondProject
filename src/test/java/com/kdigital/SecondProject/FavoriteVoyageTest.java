package com.kdigital.SecondProject;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import com.kdigital.SecondProject.service.FavoriteVoyageService;


/**
 * 선호 항해 관련 DB의 테스트 코드입니다. 
 * */
@SpringBootTest
public class FavoriteVoyageTest {
	@Autowired
	   private FavoriteVoyageService service;
	
	/**
	 * 선호 항해 등록 테스트
	 * */
	//@Test
	@WithMockUser(username="user001", roles= {"USER"})
	void searchFavVoyage() {
		boolean result = service.favorite((long) 135);
		System.out.println("Test result: " + result);
		//assertTrue(result);
	}
	
	/**
	 * 항해 즐겨찾기 변경 테스트
	 * 항해 존재: 95,5
	 * */
	//@Test
	@WithMockUser(username="user001", roles= {"USER"})
	void changeFav() {
		service.changeTopFavorite((long) 95);
	}
	
	/**
	 * 항해 저장 삭제 테스트
	 * */
	//@Test
	@WithMockUser(username="user002", roles= {"USER"})
	void deleteFev() {
		service.deleteFev((long)5);
	}
	
	
	
	/**
	 * 선호 상태 여부 파악(아이디, 항해번호)
	 * */
	//@Test
	@WithMockUser(username="user002", roles= {"USER"})
	void checkFav() {
		boolean result = service.isExist((long) 5);
		System.out.println("Test result: " + result);
	}
	
	/**
	 * 즐겨찾기 선박 추출
	 * */
	//@Test
	@WithMockUser(username="user002", roles= {"USER"})
	void savedasMostFev() {
		service.getTopFavoriteVoyage();
	}
	
	/**
	 * 사용자를 기준으로 전체 선호 항해 조회
	 * */
	@Test
	@WithMockUser(username="user001", roles= {"USER"})
	void getAllFav() {
		service.findAll();
	}	
	
}
