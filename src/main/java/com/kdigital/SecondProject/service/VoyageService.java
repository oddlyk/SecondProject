package com.kdigital.SecondProject.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Hibernate;
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
		// 콜사인으로 항해 검색
		List<VoyageEntity> entityList = voyageRepository.findByShip_CallSign(callSign);
		log.info("추출된 수: {}",entityList.size());
		// 항해가 추출되어 왔으면 on-boarding이 1인 항해를 찾아 return
		for(VoyageEntity entity : entityList) {
			log.info("OnBoarding: {}",entity.getOnBoarding());
			if("1".equals(entity.getOnBoarding())) {
				log.info("항해: {}",entity.toString());
				return VoyageDTO.toDTO(entity);
			}
		}
		/* if(entityList.size()!=0) {
			for(VoyageEntity entity : entityList) {
				if(entity.getOnBoarding().equals("1")) {
					log.info("항해: {}",entity.toString());
					return VoyageDTO.toDTO(entity);
				}
			}
			// on-boarding이 0인 항해만 존재함.
			log.info("살아있는 항해를 찾을 수 없었습니다");
			return null;
		}*/
		// 항해가 없었음.
		log.info("해당 항해를 찾을 수 없었습니다");
		return null;
	}
	
	
	/**
	 * 저장된 항해 중 특정 mmsi 선박의 항해 찾기
	 * @param mmsi
	 * @return voyageDTO
	 * */
	public VoyageDTO selectVoyageWithMmsi(String mmsi) {
		log.info("항해 정보를 mmsi로 검색합니다. mmsi명은 {} 입니다.",mmsi);
		// mmsi로 항해 검색
		List<VoyageEntity> entityList = voyageRepository.findByShip_Mmsi(mmsi);
		// 항해가 추출되어 왔으면 on-boarding이 1인 항해를 찾아 return
		if(!entityList.isEmpty()) {
			for(int i = 0;i<entityList.size();++i) {
				VoyageEntity temp = entityList.get(i);
				if (temp.getOnBoarding().equals("1")) {
					log.info("항해: {}",temp.toString());
					return VoyageDTO.toDTO(temp);
				}
			}
			// on-boarding이 0인 항해만 존재함.
			log.info("살아있는 항해를 찾을 수 없었습니다");
			return null;
		}
		// 항해가 없었음.
		log.info("해당 항해를 찾을 수 없었습니다");
		return null;
	}
	
	
	/**
	 * 저장된 항해 중 특정 imo 선박의 항해 찾기
	 * @param imo
	 * @return voyageDTO
	 * */
	public VoyageDTO selectVoyageWithImo(String imo) {
		log.info("항해 정보를 imo로 검색합니다. imo명은 {} 입니다.",imo);
		// imo로 항해 검색
		List<VoyageEntity> entityList = voyageRepository.findByShip_Imo(imo);
		// 항해가 추출되어 왔으면 on-boarding이 1인 항해를 찾아 return
		if(!entityList.isEmpty()) {
			for(int i = 0;i<entityList.size();++i) {
				VoyageEntity temp = entityList.get(i);
				if (temp.getOnBoarding().equals("1")) {
					log.info("항해: {}",temp.toString());
					return VoyageDTO.toDTO(temp);
				}
			}
			// on-boarding이 0인 항해만 존재함.
			log.info("살아있는 항해를 찾을 수 없었습니다");
			return null;
		}
		// 항해가 없었음.
		log.info("해당 항해를 찾을 수 없었습니다");
		return null;
	}
	
}
