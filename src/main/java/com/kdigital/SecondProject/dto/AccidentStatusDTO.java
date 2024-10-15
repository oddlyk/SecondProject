package com.kdigital.SecondProject.dto;

import com.kdigital.SecondProject.entity.AccidentStatusEntity;
import com.kdigital.SecondProject.entity.PortEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AccidentStatusDTO {
	private String accidentDate;
	
	private PortEntity port;
    private String firstRank;
    private double firstPer;
    private String secondRank;
    private double secondPer;
    private String thirdRank;
    private double thirdPer;
    
 // 엔티티 -> DTO 변환 메서드
    public static AccidentStatusDTO toDTO(AccidentStatusEntity accidentStatusentity) {
        return AccidentStatusDTO.builder()
                .accidentDate(accidentStatusentity.getAccidentDate())
                .port(accidentStatusentity.getPort())
                .firstRank(accidentStatusentity.getFirstRank())
                .firstPer(accidentStatusentity.getFirstPer())
                .secondRank(accidentStatusentity.getSecondRank())
                .secondPer(accidentStatusentity.getSecondPer())
                .thirdRank(accidentStatusentity.getThirdRank())
                .thirdPer(accidentStatusentity.getThirdPer())
                .build();
    }
}
