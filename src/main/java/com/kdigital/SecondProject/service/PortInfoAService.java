package com.kdigital.SecondProject.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kdigital.SecondProject.dto.PortInfoADTO;
import com.kdigital.SecondProject.entity.PortInfoAEntity;
import com.kdigital.SecondProject.repository.PortInfoARepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PortInfoAService {
	final PortInfoARepository portInfoARepository;

    /**
     * 특정 항구 코드에 해당하는 PortInfoA 데이터 조회
     * @param portCode 항구 코드
     * @return List<PortInfoADTO>
     */
	@Transactional
    public List<PortInfoADTO> getPortInfoByPortCode(String portCode) {
		log.info("항구 코드 {}에 해당하는 PortInfoA 데이터를 조회합니다.", portCode);
		List<PortInfoAEntity> portInfoAEntities = portInfoARepository.findByPort_PortCode(portCode);
		List<PortInfoADTO> dtoList = new ArrayList<>();
		if(!portInfoAEntities.isEmpty()) {
			log.info("총 개수: {}",portInfoAEntities.size());
			log.info("첫번째 값: {}",portInfoAEntities.get(0).toString());

			return portInfoAEntities.stream()
	                .map(PortInfoADTO::toDTO)  // Entity -> DTO 변환
	                .collect(Collectors.toList());
		}
		log.info("데이터가 없습니다.");
        return null;
    }
	
	 /**
     * 특정 loc_type에 해당하는 PortInfoA 데이터 조회
     * @param locType 위치 타입 (예: 1 인근 대기지)
     * @return List<PortInfoADTO>
     */
    @Transactional
    public List<PortInfoADTO> getPortInfoByLocType(int locType) {
        log.info("loc_type {}에 해당하는 PortInfoA 데이터를 조회합니다.", locType);
        List<PortInfoAEntity> portInfoAEntities = portInfoARepository.findByLocType(locType);
        return portInfoAEntities.stream()
                .map(PortInfoADTO::toDTO)  // Entity -> DTO 변환
                .collect(Collectors.toList());
    }
	
    public List<PortInfoADTO> getPortInfoByPortCodeAndLocType(String portCode, int locType) {
        List<PortInfoAEntity> entities = portInfoARepository.findByPort_PortCodeAndLocType(portCode, locType);
        if (entities.isEmpty()) {
            log.info("항구 코드 {}와 loc_type {}에 해당하는 데이터가 없습니다.", portCode, locType);
            return new ArrayList<>(); // 빈 리스트 반환
        }
        return entities.stream()
                .map(PortInfoADTO::toDTO)
                .collect(Collectors.toList());
    }
    

    /**
     * 모든 PortInfoA 데이터 조회
     * @return List<PortInfoADTO>
     */
    public List<PortInfoADTO> getAllPortInfo() {
        log.info("모든 PortInfoA 데이터를 조회합니다.");
        List<PortInfoAEntity> portInfoAEntities = portInfoARepository.findAll();

        return portInfoAEntities.stream()
                .map(PortInfoADTO::toDTO)  // Entity -> DTO 변환
                .collect(Collectors.toList());
    }
}
