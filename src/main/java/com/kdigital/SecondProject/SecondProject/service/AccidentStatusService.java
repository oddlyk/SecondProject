package com.kdigital.SecondProject.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kdigital.SecondProject.dto.AccidentStatusDTO;
import com.kdigital.SecondProject.entity.AccidentStatusEntity;
import com.kdigital.SecondProject.repository.AccidentStatusRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccidentStatusService {
	
	@Autowired
	private AccidentStatusRepository accidentStatusRepository;
	
	 /**
     * 포트 코드를 기반으로 사고 상태 조회
     * @param portCode
     * @return List<AccidentStatusDTO>
     */
    public List<AccidentStatusDTO> getAccidentStatusByPortCode(String portCode) {
        log.info("항구 코드 {}에 대한 사고 상태를 조회합니다.", portCode);
        List<AccidentStatusEntity> accidentStatusEntities = accidentStatusRepository.findByPort_PortCode(portCode);

        return accidentStatusEntities.stream()
                .map(AccidentStatusDTO::toDTO)  // Entity를 DTO로 변환
                .collect(Collectors.toList());
    }

    /**
     * 모든 사고 상태 조회
     * @return List<AccidentStatusDTO>
     */
    public List<AccidentStatusDTO> getAllAccidentStatuses() {
        log.info("모든 사고 상태를 조회합니다.");
        List<AccidentStatusEntity> accidentStatusEntities = accidentStatusRepository.findAll();

        return accidentStatusEntities.stream()
                .map(AccidentStatusDTO::toDTO)  // Entity를 DTO로 변환
                .collect(Collectors.toList());
    }
}
