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
