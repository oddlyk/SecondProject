package com.kdigital.SecondProject.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.kdigital.SecondProject.entity.FavoriteVoyageEntity;
import com.kdigital.SecondProject.entity.UserEntity;
import com.kdigital.SecondProject.entity.VoyageEntity;
import com.kdigital.SecondProject.repository.FavoriteVoyageRepository;
import com.kdigital.SecondProject.repository.UserRepository;
import com.kdigital.SecondProject.repository.VoyageRepository;
import com.mysql.cj.log.Log;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteVoyageService {

	final UserRepository userRepository;
	final VoyageRepository voyageRepository;
	final FavoriteVoyageRepository favoriteVoyageRepository;
	
	/**
	 * 선호 항해 등록
	 * @param callSign
	 * @return
	 */
	public boolean favorite(String callSign) {
		// 1. 항해 정보 확인
		List<VoyageEntity> voyageEntity = voyageRepository.findVoyageWithShipByCallSign(callSign);
		if (voyageEntity.isEmpty()) return false;
		
		VoyageEntity voyEntity = voyageEntity.get(0);
		
		// 2. 현재 사용자 정보 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userId = "user001"; //임시 추후 아래 주석을 해제하고 현재 값을 null로 초기화 할 것
		
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			userId = userDetails.getUsername();  // 이 값이 로그인된 사용자의 userId
		}
		
		// 2-1. 사용자 정보 찾기
		Optional<UserEntity> userEntity1 = userRepository.findById(userId);
		if (userEntity1.isEmpty()) {
			log.warn("User with ID {} not Found", userId);
			return false;
		}
		
		UserEntity userEntity = userEntity1.get();
		
		// 선호 선박 등록
		FavoriteVoyageEntity newFavEntity = FavoriteVoyageEntity.builder()
				.userEntity(userEntity)
				.voyageEntity(voyEntity)
				.topFavorite("0")
				.build();
		
		FavoriteVoyageEntity temp = favoriteVoyageRepository.save(newFavEntity);
		log.info("결과: {}", temp);
		
		return true;
	}
	


	/**
	 * 이미 등록된 정보인지 확인
	 * @param userId
	 * @param vNumber
	 * @return
	 */
	public boolean isExist(String userId, int vNumber) {
		Optional<FavoriteVoyageEntity> favVoyageEntity = favoriteVoyageRepository.findByUserIdAndVNumber(userId, vNumber);
		log.info("결과: {}", favVoyageEntity);
		if (favVoyageEntity.isPresent()) return true;
		else return false;
	}
}
