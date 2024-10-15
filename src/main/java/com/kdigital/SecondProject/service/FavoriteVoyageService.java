package com.kdigital.SecondProject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.kdigital.SecondProject.dto.FavoriteVoyageDTO;
import com.kdigital.SecondProject.entity.FavoriteVoyageEntity;
import com.kdigital.SecondProject.entity.UserEntity;
import com.kdigital.SecondProject.entity.VoyageEntity;
import com.kdigital.SecondProject.repository.FavoriteVoyageRepository;
import com.kdigital.SecondProject.repository.UserRepository;
import com.kdigital.SecondProject.repository.VoyageRepository;

import jakarta.transaction.Transactional;
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
	 @Transactional
	public boolean favorite(String callSign) {
		// 1. 항해 정보 확인
		List<VoyageEntity> voyageEntity = voyageRepository.findVoyageWithShipByCallSign(callSign);
		if (voyageEntity.isEmpty()) return false;
		
		VoyageEntity voyEntity = voyageEntity.get(0);
		
		
		// 2. 현재 사용자 정보 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userId = "null";
		
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			userId = userDetails.getUsername();  // 이 값이 로그인된 사용자의 userId
		}
		else {
			log.info("로그인되어있지 않음.");
			return false;
		}
		
		
		// 2-1. 사용자 존재 여부 조회
		Optional<UserEntity> userEntity1 = userRepository.findById(userId);
		if (userEntity1.isEmpty()) {
			log.warn("해당 사용자의 정보가 존재하지 않음: {}", userId);
			return false;
		}
		
		UserEntity userEntity = userEntity1.get();
		
		// 사용자+항해를 결합하여 저장용으로 생성
		FavoriteVoyageEntity newFavEntity = FavoriteVoyageEntity.builder()
				.userEntity(userEntity)
				.voyageEntity(voyEntity)
				.topFavorite("0")
				.build();
		
		//해당 사용자가 해당 항해를 이미 선호 항해로 저장하였는가를 확인
		boolean result = isExist(voyEntity.getVNumber());
		log.info("isExist 결과: {}", result);
		if (result) {
			log.info("이미 저장된 항해입니다.");
			return false;
		}
		//사용자가 등록한 선호 항해의 수가 10개를 초과하는지 확인
		long count = favoriteVoyageRepository.countByUserEntity_UserId(userId);
		if(count>=10) {
			log.info("해당 사용자의 선호 항해 10개가 저장되어있습니다.");
			return false;
		}
		
		FavoriteVoyageEntity temp = favoriteVoyageRepository.save(newFavEntity);
		log.info("선호 항해 등록 완료: {}", temp.toString());
		return true;
	}
	
	
	/**
	 * 항해 즐겨 찾기 지정 및 수정
	 * @param vNumber
	 * @return true/false
	 * */
	@Transactional
	public boolean changeTopFavorite(Long vNumber) {
		
		// 현재 사용자 정보 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userId = "null";
		
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			userId = userDetails.getUsername();  // 이 값이 로그인된 사용자의 userId
		}
		else {
			log.info("로그인되지 않음");
			return false;
		}
		
		// 사용자 정보 찾기
		Optional<UserEntity> userEntity1 = userRepository.findById(userId);
		if (userEntity1.isEmpty()) {
			log.warn("사용자 정보를 찾지 못함: {}", userId);
			return false;
		}
		UserEntity userEntity = userEntity1.get();

		// 기존에 topFavorite이 1인 항해가 있다면, 0으로 업데이트
		Optional<FavoriteVoyageEntity> existingFavorite = favoriteVoyageRepository.findByUserEntityAndTopFavorite(userEntity, "1");
		if (existingFavorite.isPresent()) {
		FavoriteVoyageEntity favoriteVoyageEntity = existingFavorite.get();
		favoriteVoyageEntity.setTopFavorite("0");
		FavoriteVoyageEntity temp = favoriteVoyageRepository.save(favoriteVoyageEntity);
		log.info("즐겨찾기를 해제하였습니다. : {}",temp);
		}
		
		// 방금 선택한 항해를 topFavorite으로 지정 (1로 업데이트)
		Optional<FavoriteVoyageEntity> favVoyageEntity = favoriteVoyageRepository.findByUserEntity_UserIdAndVoyageEntity_vNumber(userId, vNumber);
		if (favVoyageEntity.isPresent()) {
			FavoriteVoyageEntity favoriteVoyageEntity = favVoyageEntity.get();
			favoriteVoyageEntity.setTopFavorite("1");
			FavoriteVoyageEntity temp = favoriteVoyageRepository.save(favoriteVoyageEntity);
			log.info("즐겨찾기로 지정되었습니다.: {}",temp);
			return true;
		}
		log.info("즐겨찾기 지정 실패");
		return false;
	}

	
	/**
	 * 선호 항해 삭제
	 * @param vNumber
	 * @return true/false
	 * */
	@Transactional
	public boolean deleteFev(Long vNumber) {
		// 현재 사용자 정보 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userId = "null";
		
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			userId = userDetails.getUsername();  // 이 값이 로그인된 사용자의 userId
		}
		else {
			log.info("사용자 정보를 찾지 못함");
			return false;
		}
		
		// 사용자 정보 찾기
		Optional<UserEntity> userEntity1 = userRepository.findById(userId);
		if (userEntity1.isEmpty()) {
			log.warn("사용자 정보를 찾지 못함: {}", userId);
			return false;
		}
		Optional<FavoriteVoyageEntity> favVoyageEntity = favoriteVoyageRepository.findByUserEntity_UserIdAndVoyageEntity_vNumber(userId, vNumber);
		if (favVoyageEntity.isPresent()) {
			favoriteVoyageRepository.deleteById(favVoyageEntity.get().getFsNumber());
			log.info("선호 항해를 삭제했습니다.");
			return true;
		}
		log.info("선호 항해로 저장되어 있지 않았습니다.");
		return false;
	}
	
	
	/**
	 * 이미 선호로 등록된 항해인지 확인
	 * @param vNumber
	 * @return true/false
	 */
	@Transactional
	public boolean isExist(Long vNumber) {
		// 현재 사용자 정보 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userId = "null";
		
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			userId = userDetails.getUsername();  // 이 값이 로그인된 사용자의 userId
		}
		else {
			log.info("사용자 정보를 찾지 못함");
			return false;
		}
		
		// 사용자 정보 찾기
		Optional<UserEntity> userEntity1 = userRepository.findById(userId);
		if (userEntity1.isEmpty()) {
			log.warn("사용자 정보를 찾지 못함: {}", userId);
			return false;
		}
		Optional<FavoriteVoyageEntity> favVoyageEntity = favoriteVoyageRepository.findByUserEntity_UserIdAndVoyageEntity_vNumber(userId, vNumber);
		log.info("선호 항해 등록 여부: {}", favVoyageEntity.isPresent());
		if (favVoyageEntity.isPresent()) return true;
		else return false;
	}
	

	/**
	 * 즐겨찾기 항해 조회
	 * @return FavoriteVoyageDTO
	 * */
	@Transactional
	public FavoriteVoyageDTO getTopFavoriteVoyage() {
		// 현재 사용자 정보 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userId = "null";
		
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			userId = userDetails.getUsername();  // 이 값이 로그인된 사용자의 userId
		}
		else {
			log.info("사용자 정보를 찾지 못함");
			return null;
		}
		
		// 사용자 정보 찾기
		Optional<UserEntity> userEntity1 = userRepository.findById(userId);
		if (userEntity1.isEmpty()) {
			log.warn("사용자 정보를 찾지 못함: {}", userId);
			return null;
		}
		UserEntity userEntity = userEntity1.get();

		// topFavorite이 1인 항해 조회
		Optional<FavoriteVoyageEntity> existingFavorite = favoriteVoyageRepository.findByUserEntityAndTopFavorite(userEntity, "1");
		if (existingFavorite.isPresent()) {
		FavoriteVoyageEntity favoriteVoyageEntity = existingFavorite.get();
		FavoriteVoyageDTO dto = FavoriteVoyageDTO.toDTO(favoriteVoyageEntity);
		log.info("즐겨찾기된 선박: {}",dto.toString());
		return dto;
		}
		log.info("즐겨찾기된 항해가 존재하지 않습니다.");
		return null;
	}
	
	
	/**
	 * 사용자를 기준으로 전체 선호 항해 추출
	 * */
	@Transactional
	public List<FavoriteVoyageDTO> findAll(){
		// 현재 사용자 정보 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userId = "null";
		
		if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			userId = userDetails.getUsername();  // 이 값이 로그인된 사용자의 userId
		}
		else {
			log.info("사용자 정보를 찾지 못함");
			return null;
		}
		
		// 사용자 정보 찾기
		Optional<UserEntity> userEntity1 = userRepository.findById(userId);
		if (userEntity1.isEmpty()) {
			log.warn("사용자 정보를 찾지 못함: {}", userId);
			return null;
		}
		UserEntity userEntity = userEntity1.get();
		log.info("사용자 {}의 전체 선호 항해를 출력합니다.",userId);
		List<FavoriteVoyageEntity> entityList = favoriteVoyageRepository.findAllByUserEntity_UserId(userId);
		log.info("저장된 선호 항해는 총 {}개 입니다.",entityList.size());
		List<FavoriteVoyageDTO> dtoList = new ArrayList<>();
		if(!entityList.isEmpty()) {
			log.info("가장 상단에 저장된 항해: {}",entityList.get(0).toString());
			for(FavoriteVoyageEntity entity : entityList) {
				dtoList.add(FavoriteVoyageDTO.toDTO(entity));
			}
			return dtoList;
		}
		log.info("선호 항해가 존재하지 않습니다.");
		return null;
	}
	
	
}
