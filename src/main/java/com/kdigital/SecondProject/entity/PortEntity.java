package com.kdigital.SecondProject.entity;

import com.kdigital.SecondProject.dto.PortDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor /*Entity도 생성자가 존재해야 합니다! */
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
@Entity
@Table(name = "port")
public class PortEntity {
	@Id
	@Column(name = "port_code")
	private String portCode;
	
	@Column(name = "port_name")
    private String portName;

    @Column(name = "port_lat")
    private double portLat;

    @Column(name = "port_lon")
    private double portLon;

    @Column(name = "port_contact")
    private String portContact;

    @Column(name = "port_url")
    private String portUrl;

    @Column(name = "port_addr")
    private String portAddr;

    @Column(name = "avg_working_time")
    private double avgWorkingTime;

    @Column(name = "avg_waiting_time")
    private double avgWaitingTime;
    
 // DTO -> Entity 변환 (정적 메서드)
    public static PortEntity toEntity(PortDTO portDTO) {
        return PortEntity.builder()
                .portCode(portDTO.getPortCode())
                .portName(portDTO.getPortName())
                .portLat(portDTO.getPortLat())
                .portLon(portDTO.getPortLon())
                .portContact(portDTO.getPortContact())
                .portUrl(portDTO.getPortUrl())
                .portAddr(portDTO.getPortAddr())
                .avgWorkingTime(portDTO.getAvgWorkingTime())
                .avgWaitingTime(portDTO.getAvgWaitingTime())
                .build();
    }
}
