package com.kdigital.SecondProject.dto;

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
@ToString
@Builder
public class PortDTO {
	private String portCode;
	private String portName;
	private double portLat;
	private double portLon;
	private String portContact;
	private String portUrl;
	private String portAddr;
	private double avgWorkingTime;
	private double avgWaitingTime;
	
	// 엔티티 -> DTO 변환 메서드
    public static PortDTO toDTO(PortEntity portEntity) {
        return PortDTO.builder()
                .portCode(portEntity.getPortCode())
                .portName(portEntity.getPortName())
                .portLat(portEntity.getPortLat())
                .portLon(portEntity.getPortLon())
                .portContact(portEntity.getPortContact())
                .portUrl(portEntity.getPortUrl())
                .portAddr(portEntity.getPortAddr())
                .avgWorkingTime(portEntity.getAvgWorkingTime())
                .avgWaitingTime(portEntity.getAvgWaitingTime())
                .build();
    }
}
