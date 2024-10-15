package com.kdigital.SecondProject.entity;

import com.kdigital.SecondProject.dto.AccidentStatusDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "accident_status")
public class AccidentStatusEntity {
	
	@Id
	@Column(name = "accident_date")
	private String accidentDate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="port_code")
	private PortEntity port;
	
	@Column(name = "first_rank")
	private String firstRank;
	
	@Column(name = "first_per")
	private double firstPer;
	
	@Column(name = "second_rank")
	private String secondRank;
	
	@Column(name = "second_per")
	private double secondPer;
	
	@Column(name = "third_rank")
	private String thirdRank;
	
	@Column(name = "third_per")
	private double thirdPer;

	// dto -> entity 변환 메서드
    public static AccidentStatusEntity toEntity(AccidentStatusDTO accidentStatusDTO) {
        return AccidentStatusEntity.builder()
                .accidentDate(accidentStatusDTO.getAccidentDate())
                .port(accidentStatusDTO.getPort())
                .firstRank(accidentStatusDTO.getFirstRank())
                .firstPer(accidentStatusDTO.getFirstPer())
                .secondRank(accidentStatusDTO.getSecondRank())
                .secondPer(accidentStatusDTO.getSecondPer())
                .thirdRank(accidentStatusDTO.getThirdRank())
                .thirdPer(accidentStatusDTO.getThirdPer())
                .build();
    }
}
