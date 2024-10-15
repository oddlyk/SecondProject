package com.kdigital.SecondProject.service;

import java.util.List;
import java.util.stream.Collectors;


import org.springframework.stereotype.Service;

import com.kdigital.SecondProject.dto.PortInfoBDTO;
import com.kdigital.SecondProject.entity.PortInfoBEntity;
import com.kdigital.SecondProject.repository.PortInfoBRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PortInfoBService {

	final PortInfoBRepository portInfoBRepository;
	
	/**
	 * 특정 항구 코드에 해당하는 PortInfoB 데이터 조회
	 * @param portCode 항구 코드
	 * @return List<PortInfoBDTO>
	 */
	@Transactional
	public List<PortInfoBDTO> getPortInfoByPortCode(String portCode) {
		log.info("항구 코드 {}에 해당하는 PortInfoB 데이터를 조회합니다.", portCode);
		List<PortInfoBEntity> portInfoBEntities = portInfoBRepository.findByPort_PortCode(portCode);
		if(!portInfoBEntities.isEmpty()) {
			log.info("총 개수: {}",portInfoBEntities.size());
			log.info("첫번째 값: {}",portInfoBEntities.get(0).toString());

			return portInfoBEntities.stream()
	                .map(PortInfoBDTO::toDTO)  // Entity -> DTO 변환
	                .collect(Collectors.toList());
		}
		log.info("데이터가 없습니다.");
        return null;
	}
		/**
	     * 특정 위치 타입에 해당하는 PortInfoB 데이터 조회
	     * @param locType 위치 타입
	     * @return List<PortInfoBDTO>
	     */
	    public List<PortInfoBDTO> getPortInfoByLocType(int locType) {
	        log.info("위치 타입 {}에 해당하는 PortInfoB 데이터를 조회합니다.", locType);
	        List<PortInfoBEntity> portInfoBEntities = portInfoBRepository.findByLocType(locType);

	        return portInfoBEntities.stream()
	                .map(PortInfoBDTO::toDTO)  // Entity -> DTO 변환
	                .collect(Collectors.toList());
	    }

	    /**
	     * 모든 PortInfoB 데이터 조회
	     * @return List<PortInfoBDTO>
	     */
	    public List<PortInfoBDTO> getAllPortInfoB() {
	        log.info("모든 PortInfoB 데이터를 조회합니다.");
	        List<PortInfoBEntity> portInfoBEntities = portInfoBRepository.findAll();

	        return portInfoBEntities.stream()
	                .map(PortInfoBDTO::toDTO)  // Entity -> DTO 변환
	                .collect(Collectors.toList());
	    }
	
}
