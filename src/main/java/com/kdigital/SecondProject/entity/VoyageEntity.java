package com.kdigital.SecondProject.entity;

import java.time.LocalDateTime;


import com.kdigital.SecondProject.dto.VoyageDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder

@Entity
@Table(name="voyage")
public class VoyageEntity {
	@Id
	@Column(name="v_number")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long vNumber;
	
	@Column(name="call_sign")
	private String callSign;
	
	@Column(name="departure_date")
	private LocalDateTime departureDate;
	
	@Column(name="arrival_date")
	private LocalDateTime arrivalDate;
	
	@Column(name="departure")
	private String departure;
	
	@Column(name="destination")
	private String destination;
	
	@Column(name="on_boarding")
	private String onBoarding;
	
	@Column(name="extra_tonnage")
	private int extraTonnage;
	
	@Column(name="entry_exit_fee")
	private int entryExitFee;
	
	@Column(name="berthing_fee")
	private int berthingFee;
	
	@Column(name="anchorage_fee")
	private int anchorageFee;
	
	@Column(name="security_fee")
	private int securityFee;
	
	
	//dto->entity
	public static VoyageEntity toEntity(VoyageDTO dto) {
		return VoyageEntity.builder()
		.vNumber(dto.getVNumber())
		.callSign(dto.getCallSign())
		.departureDate(dto.getDepartureDate())
		.arrivalDate(dto.getArrivalDate())
		.departure(dto.getDeparture())
		.destination(dto.getDestination())
		.onBoarding(dto.getOnBoarding())
		.extraTonnage(dto.getExtraTonnage())
		.entryExitFee(dto.getEntryExitFee())
		.berthingFee(dto.getBerthingFee())
		.anchorageFee(dto.getAnchorageFee())
		.securityFee(dto.getSecurityFee())
		.build();
	}
}
