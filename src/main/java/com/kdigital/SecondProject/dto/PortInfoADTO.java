package com.kdigital.SecondProject.dto;

import com.kdigital.SecondProject.entity.PortEntity;
import com.kdigital.SecondProject.entity.PortInfoAEntity;

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
public class PortInfoADTO {
	private int piaNumber;      
    private PortEntity port;    
    private int locType;        
    private String info;        
    private String location;    
    private Integer minTon;     
    private Integer maxTon;     
    
 // 엔티티 -> DTO 변환 메서드
    public static PortInfoADTO toDTO(PortInfoAEntity portInfoAEntity) {
        return PortInfoADTO.builder()
                .piaNumber(portInfoAEntity.getPiaNumber())
                .port(portInfoAEntity.getPort())  // PortEntity를 그대로 받음
                .locType(portInfoAEntity.getLocType())
                .info(portInfoAEntity.getInfo())
                .location(portInfoAEntity.getLocation())
                .minTon(portInfoAEntity.getMinTon())
                .maxTon(portInfoAEntity.getMaxTon())
                .build();
    }
    
}
