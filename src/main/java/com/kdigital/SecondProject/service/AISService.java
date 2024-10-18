package com.kdigital.SecondProject.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kdigital.SecondProject.dto.AISDTO;
import com.kdigital.SecondProject.entity.AISEntity;
import com.kdigital.SecondProject.repository.AISRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AISService {
	//findByVoyage_VNumber
	final AISRepository aisRepository;
	
	/**
	 * 항해 번호로 해당 항해의 ais 신호 전체 추출해오기
	 * @param vNumber
	 * @return List<AISDTO>
	 * */
	@Transactional
	public List<AISDTO> selectAISAll(Long vNumber) {
		log.info("항해를 기준으로 모든 ais 신호를 추출합니다. 항해명은 {} 입니다.",vNumber);
		// 항해 번호로 ais 신호 추출
			// 외래키는 vNumber에만 연결되어 있음에 유의.
			// 현재 날짜, 시간을 기준으로 2년 전까지의 신호만 오름차순 정렬하여 추출해 옴.
		List<AISEntity> entityList = aisRepository.findVoyageWithCallSignAsAISEntity(vNumber);
		List<AISDTO> dtoList = new ArrayList<>();
		
		if(!entityList.isEmpty()) {
			log.info("존재하는 전체 신호의 수는 총 {}개 입니다.",entityList.size());
			log.info("첫번째 신호: {}",entityList.get(0).toString());
			for (AISEntity entity : entityList) {
				dtoList.add(AISDTO.toDTO(entity));
			}
			return dtoList;
		}
		// 해당 항해의 신호가 없음
		log.info("해당 항해의 신호가 존재하지 않습니다.");
		return null;
	}
	
	/**
	 * 가장 최근 항해의 ais 신호 추출
	 * @param vNumber
	 * @return AISDTO
	 * */
	@Transactional
	public AISDTO currentAISsignal(Long vNumber) {
		log.info("항해를 기준으로 가장 최근의 ais 신호를 추출합니다. 항해명은 {}입니다.",vNumber);
		
		 LocalDateTime twoYearsAgo = LocalDateTime.now().minusYears(2);
		Optional<AISEntity> entity = aisRepository.findFirstByVoyage_vNumberAndSignalDateBeforeOrderBySignalDateDesc(vNumber,twoYearsAgo);
		if(entity.isPresent()) {
			AISDTO dto = AISDTO.toDTO(entity.get());
			log.info("추출된 가장 최근의 ais 신호: {}",dto.getSignalDate());
			return dto;
		}
		log.info("(Service) 항해 시작 전");
		return null;
	}
}
