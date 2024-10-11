package com.kdigital.SecondProject.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.kdigital.SecondProject.dto.ShipDTO;
import com.kdigital.SecondProject.entity.ShipEntity;
import com.kdigital.SecondProject.repository.ShipRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipService {
	final ShipRepository shipRepository;
	
	/**
	 * 저장된 선박 중 특정 콜사인의 선박 찾기
	 * @param callSign
	 * @return shipDTO
	 * */
	public ShipDTO selectOneShip(String callSign) {
		log.info("선박 정보를 검색합니다. 검색할 선박의 콜사인은 {} 입니다.",callSign);
		Optional<ShipEntity> entity = shipRepository.findById(callSign); 
		if(entity.isPresent()) {
			ShipEntity temp = entity.get();
			ShipDTO shipDTO = ShipDTO.toDTO(temp);
			log.info("찾아온 선박의 정보: {}",shipDTO.toString());
			return shipDTO;
		}
		log.info("해당 선박을 찾을 수 없었습니다.");
		return null;
	}
}
