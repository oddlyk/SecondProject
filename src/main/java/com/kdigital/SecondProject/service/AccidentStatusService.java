package com.kdigital.SecondProject.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kdigital.SecondProject.dto.AccidentStatusDTO;
import com.kdigital.SecondProject.entity.AccidentStatusEntity;
import com.kdigital.SecondProject.repository.AccidentStatusRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccidentStatusService {
	final AccidentStatusRepository accidentStatusRepository;
	
	 /**
     * 포트 코드를 기반으로 사고 상태 조회
     * @param portCode
     * @return List<AccidentStatusDTO>
     */
	@Transactional
    public List<AccidentStatusDTO> getAccidentStatusByPortCode(String portCode) {
		log.info("항구 코드 {}에 대한 전년도 동일 월의 사고 상태를 조회합니다.", portCode);
		
		// 현재 날짜에서 전년도 동일 월 계산
        LocalDate currentDate = LocalDate.now();
        String previousYearMonth = (currentDate.getYear() - 1) + "_" + String.format("%02d", currentDate.getMonthValue());
		
        // "YYYY_MM" 형태로 accidentDate가 전년도 동일 월에 해당하는 데이터만 필터링
        List<AccidentStatusEntity> accidentStatusEntities = accidentStatusRepository.findByPort_PortCode(portCode)
            .stream()
            .filter(entity -> entity.getAccidentDate().startsWith(previousYearMonth))  // 전년도 동일 월 필터링
            .collect(Collectors.toList());
        
        if (!accidentStatusEntities.isEmpty()) {
            log.info("첫번째 사고 정보: {}", accidentStatusEntities.get(0).toString());
        }

        return accidentStatusEntities.stream()  
        		// .stream() : 리스트를 스트림으로 변환, 데이터 처리 연속성
                .map(AccidentStatusDTO::toDTO)  // Entity -> DTO 변환
                .collect(Collectors.toList());
        
        // .map() : 스트림 내 각 요소에 대해 변환 작업 수행
        // -> AccidentStatusEntity 객체를 AccidentStatusDTO 로 변환
        // 스트림 내의 각 엔티티 객체를 DTO로 변환한 결과를 반환하는 메소드
        // collect() : 스트림으로 변환된 데이터를 리스트로 다시 수집
        
//        List<AccidentStatusEntity> accidentStatusEntities = accidentStatusRepository.findByPort_PortCode(portCode);
//        log.info("존재하는 전체 신호의 수는 총 {}개 입니다.",accidentStatusEntities.size());
//        if(!accidentStatusEntities.isEmpty()) {
//            log.info("첫번째 정보 출력: {}",accidentStatusEntities.get(0).toString());
//          return accidentStatusEntities.stream()
//                  .map(AccidentStatusDTO::toDTO)  // Entity를 DTO로 변환
//                  .collect(Collectors.toList());
//        }
//        return null;
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
