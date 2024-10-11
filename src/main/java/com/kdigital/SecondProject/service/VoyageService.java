package com.kdigital.SecondProject.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kdigital.SecondProject.dto.VoyageDTO;
import com.kdigital.SecondProject.entity.VoyageEntity;
import com.kdigital.SecondProject.repository.VoyageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoyageService {
	final VoyageRepository voyageRepository;
	
	/**
	 * 저장된 항해 중 특정 콜사인 선박의 항해 찾기
	 * @param callSign
	 * @return voyageDTO
	 * */
	public VoyageDTO selectVoyageWithCallSign(String callSign) {
		log.info("항해 정보를 콜사인으로 검색합니다. 콜사인명은 {} 입니다.",callSign);
		
		List<VoyageEntity> entityList = voyageRepository.findByShip_CallSign(callSign);
		List<VoyageDTO> dtoList = new ArrayList<>();
		if(entityList.size()!=0) {
			entityList.forEach((entity)->dtoList.add(VoyageDTO.toDTO(entity)));
			for(int i = 0;i<dtoList.size();++i) {
				log.info("{}번째 항해: {}",i,dtoList.get(i).toString());
			}
			return dtoList.get(0);
		}
		log.info("해당 항해를 찾을 수 없었습니다");
		return null;
	}
	
}
