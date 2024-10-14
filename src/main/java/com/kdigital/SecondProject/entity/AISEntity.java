package com.kdigital.SecondProject.entity;

import java.time.LocalDateTime;

import com.kdigital.SecondProject.dto.AISDTO;

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

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder

@Entity
@Table(name="ais")
public class AISEntity {
	@Id
	@Column(name="ais_number")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long aisNumber;
	
	//참조 해야 하나, 관계작성 안함.
    @Column(name="call_sign") 
	private String callSign;  // Voyage 테이블의 call_sign 참조 (외래키)
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vNumber")  // 외래 키 매핑
	private VoyageEntity voyage;   // Voyage 테이블의 v_number 참조 (외래키)
	
	@Column(name="signal_date")
	private LocalDateTime signalDate;
	
	@Column(name="latitude")
	private double latitude;
	
	@Column(name="longitude")
	private double longitude;
	
	@Column(name="speed")
	private double speed;
	
	@Column(name="direction")
	private double direction;
	
	@Column(name="departure")
	private String departure;
	
	//참조 해야 하나, 관계작성 안함.
	@Column(name="destination")
	private String destination;  // Voyage 테이블의 destination 참조 (외래키)


	
	//dto->entity
	public static AISEntity toEntity(AISDTO dto) {
		return AISEntity.builder()
				.aisNumber(dto.getAisNumber())
				.callSign(dto.getCallSign())
				.voyage(dto.getVoyage())
				.signalDate(dto.getSignalDate())
				.latitude(dto.getLatitude())
				.longitude(dto.getLongitude())
				.speed(dto.getSpeed())
				.direction(dto.getDirection())
				.departure(dto.getDeparture())
				.destination(dto.getDestination())
				.build();
	}
}
