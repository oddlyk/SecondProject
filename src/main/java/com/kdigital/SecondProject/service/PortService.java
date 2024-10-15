package com.kdigital.SecondProject.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kdigital.SecondProject.dto.PortDTO;
import com.kdigital.SecondProject.entity.PortEntity;
import com.kdigital.SecondProject.repository.PortRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PortService {
	final PortRepository portRepository; /*@Autowired는 test파일에서 사용해야 합니다.*/
	
	/**
     * 모든 항구 정보 조회
     * @return List<PortDTO>
     */
    public List<PortDTO> getAllPorts() {
        log.info("모든 항구 정보를 조회합니다.");
        List<PortEntity> entityList = portRepository.findAll();
        List<PortDTO> dtoList = new ArrayList<>();
        
        entityList.forEach(entity -> dtoList.add(PortDTO.toDTO(entity)));
        
        log.info("총 {}개의 항구 정보를 조회했습니다.", dtoList.size());
        return dtoList;
    }

    /**
     * 특정 항구 코드로 항구 정보 조회
     * @param portCode
     * @return PortDTO
     */
	@Transactional
    public PortDTO selectPortByPortCode(String portCode) {
        log.info("항구 정보를 portCode로 검색합니다. portCode는 {} 입니다.", portCode);
        
        List<PortEntity> entityList = portRepository.findByPortCode(portCode);
        if (!entityList.isEmpty()) {
            PortDTO portDTO = PortDTO.toDTO(entityList.get(0));  // 첫 번째 항구 정보 반환
            log.info("해당 항구 정보를 조회했습니다: {}", portDTO.toString());
            return portDTO;
        }
        log.info("해당 portCode에 대한 항구 정보를 찾을 수 없었습니다.");
        return null;
    }
	
	
//	// 모든 항구 리스트 조회
//	public List<PortEntity> getAllPorts() {
//		
//		return portRepository.findAll();
//	}
//	
//	// 특정 항구코드에 해당하는 항구 정보 조회
//	public PortEntity getPortById(String portCode) {
//		Optional<PortEntity> entity = portRepository.findById(portCode);
//		
//		if (entity.isPresent()) {
//	        log.info("항구 정보 조회 성공: {}", entity.toString());
//	        return entity.get();  // 항구 정보 반환
//	    } else {
//	        log.warn("항구 코드 {} 에 해당하는 항구를 찾을 수 없습니다. 기본값으로 부산항을 반환합니다.", portCode);
//	        
//	        // 기본값으로 DB에서 부산항 정보 조회하여 반환
//	        String busanPortCode = "KRPUS";  // 부산항의 port_code 값
//	        return portRepository.findById(busanPortCode)
//	                .orElseThrow(() -> new RuntimeException("부산항 정보가 DB에 없습니다."));
//	    }
	
//		if (entity.isPresent()){
//			log.info("프린트테스트 {}",entity.toString());
//			
//			return null;
//		}
//        return null;
//    }
	
	
}
