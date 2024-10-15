package com.kdigital.SecondProject.entity;

import com.kdigital.SecondProject.dto.PortInfoADTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor /*Entity도 생성자가 존재해야 합니다! */
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "port_info_a")
public class PortInfoAEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pia_number")
	private int piaNumber;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="port_code")
	private PortEntity port;
	
	@Column(name = "loc_type")
    private int locType;
    
    @Column(name = "info")
    private String info;
    
    @Column(name = "location")
    private String location;
    
    @Column(name = "min_ton")
    private Integer minTon;
    
    @Column(name = "max_ton")
    private Integer maxTon;
    
 // DTO -> Entity 변환 메서드
    public static PortInfoAEntity toEntity(PortInfoADTO portInfoAdto) {
    	return PortInfoAEntity.builder()
    			.piaNumber(portInfoAdto.getPiaNumber())
                .port(portInfoAdto.getPort())  // PortEntity를 그대로 받음
                .locType(portInfoAdto.getLocType())
                .info(portInfoAdto.getInfo())
                .location(portInfoAdto.getLocation())
                .minTon(portInfoAdto.getMinTon())
                .maxTon(portInfoAdto.getMaxTon())
                .build();
    }
}
