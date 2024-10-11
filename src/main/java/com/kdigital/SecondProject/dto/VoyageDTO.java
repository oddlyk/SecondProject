package com.kdigital.SecondProject.dto;

import java.time.LocalDateTime;

import com.kdigital.SecondProject.entity.VoyageEntity;

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
public class VoyageDTO {
	private Long vNumber;
	private String callSign;
	private LocalDateTime departureDate;
	private LocalDateTime arrivalDate;
	private String departure;
	private String destination;
	private String onBoarding;
	private int extraTonnage;
	private int entryExitFee;
	private int berthingFee;
	private int anchorageFee;
	private int securityFee;
	
	//entity->dto
	public static VoyageDTO toDTO(VoyageEntity entity) {
		return VoyageDTO.builder()
		.vNumber(entity.getVNumber())
		.callSign(entity.getCallSign())
		.departureDate(entity.getDepartureDate())
		.arrivalDate(entity.getArrivalDate())
		.departure(entity.getDeparture())
		.destination(entity.getDestination())
		.onBoarding(entity.getOnBoarding())
		.extraTonnage(entity.getExtraTonnage())
		.entryExitFee(entity.getEntryExitFee())
		.berthingFee(entity.getBerthingFee())
		.anchorageFee(entity.getAnchorageFee())
		.securityFee(entity.getSecurityFee())
		.build();
	}
}
