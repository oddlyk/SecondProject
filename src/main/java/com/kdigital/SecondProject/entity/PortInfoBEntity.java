package com.kdigital.SecondProject.entity;

import java.time.LocalDateTime;

import com.kdigital.SecondProject.dto.PortInfoBDTO;

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
@ToString
@Entity
@Builder
@Table(name = "port_info_b")
public class PortInfoBEntity {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pib_number")
    private int pibNumber;  // 고유 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "port_code")  // PortEntity의 portCode와 연결된 외래키
    private PortEntity port;

    @Column(name = "loc_type")
    private int locType;

    @Column(name = "location")
    private String location;

    @Column(name = "add_date")
    private LocalDateTime addDate;

    @Column(name = "end_date", nullable = true)
    private LocalDateTime endDate;
    
    // DTO -> Entity 변환 메서드
    public static PortInfoBEntity toDTO(PortInfoBDTO portInfoBDTO) {
        return PortInfoBEntity.builder()
                .pibNumber(portInfoBDTO.getPibNumber())
                .port(portInfoBDTO.getPort()) 
                .locType(portInfoBDTO.getLocType())
                .location(portInfoBDTO.getLocation())
                .addDate(portInfoBDTO.getAddDate())
                .endDate(portInfoBDTO.getEndDate())
                .build();
    }
}
