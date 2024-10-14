package com.kdigital.SecondProject.service;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.kdigital.SecondProject.entity.FavoriteVoyageEntity;
import com.kdigital.SecondProject.repository.FavoriteVoyageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteVoyageService {

	final VoyageRepository voyageRepository;
	final FavoriteVoyageRepository favoriteVoyageRepository;
	
	/**
	 * 선호 항해 등록
	 * @param callSign
	 * @return
	 */
	public boolean favorite(String callSign) {
		// 1. 항해 정보 확인
		Optional<VoyageEntity> voyageEntity = voyageRepository.findByCallSign(callSign);
		if (voyageEntity.isEmpty()) return false;
		
		// 2. 현재 사용자 정보 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userId = "";
		
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			userId = userDetails.getUsername();  // 이 값이 로그인된 사용자의 userId
		}
		
		// 3. topFavorite이 1인지 확인
		Optional<FavoriteVoyageEntity> existingFavorite = favoriteVoyageRepository.findByUserTopFavorite("1");
		
		// 기존에 topFavorite이 1인 경우, 0으로 업데이트
		if (existingFavorite.isPresent()) {
			FavoriteVoyageEntity favoriteVoyageEntity = existingFavorite.get();
			favoriteVoyageEntity.setTopFavorite("0");
			favoriteVoyageRepository.save(favoriteVoyageEntity);
		}
		
		// 선호 선박 등록
		FavoriteVoyageEntity newFavEntity = FavoriteVoyageEntity.builder()
				.voyageEntity(voyageEntity.get())
				.topFavorite("1")
				.build();
		
		favoriteVoyageRepository.save(newFavEntity);
		
		return true;
	}
	
}
