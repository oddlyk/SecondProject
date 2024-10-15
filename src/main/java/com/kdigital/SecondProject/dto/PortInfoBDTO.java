package com.kdigital.SecondProject.dto;

import java.time.LocalDateTime;

import com.kdigital.SecondProject.entity.PortEntity;
import com.kdigital.SecondProject.entity.PortInfoBEntity;

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
public class PortInfoBDTO {

	private int pibNumber;     
    private PortEntity port;   
    private int locType;       
    private String location;   
    private LocalDateTime addDate;    
    private LocalDateTime endDate;    

    // 엔티티 -> DTO 변환 메서드
    public static PortInfoBDTO toDTO(PortInfoBEntity portInfoBEntity) {
        return PortInfoBDTO.builder()
                .pibNumber(portInfoBEntity.getPibNumber())
                .port(portInfoBEntity.getPort())  // PortEntity를 그대로 받음
                .locType(portInfoBEntity.getLocType())
                .location(portInfoBEntity.getLocation())
                .addDate(portInfoBEntity.getAddDate())
                .endDate(portInfoBEntity.getEndDate())
                .build();
    }
}
