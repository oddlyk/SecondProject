package com.kdigital.SecondProject.dto;

import java.time.LocalDateTime;

import com.kdigital.SecondProject.entity.PortEntity;
import com.kdigital.SecondProject.entity.ShipEntity;
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
	//private String callSign;
	private ShipEntity ship;
	private LocalDateTime departureDate;
	private LocalDateTime arrivalDate;
	private String departure;
	private PortEntity port;
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
		//.callSign(entity.getCallSign()) : 외래키 참조를 넣지 않았을 때
		.ship(entity.getShip()) // 외래키 참조 시, ship 객체를 통째로 가져옴
		.departureDate(entity.getDepartureDate())
		.arrivalDate(entity.getArrivalDate())
		.departure(entity.getDeparture())
		.port(entity.getPort())
		.onBoarding(entity.getOnBoarding())
		.extraTonnage(entity.getExtraTonnage())
		.entryExitFee(entity.getEntryExitFee())
		.berthingFee(entity.getBerthingFee())
		.anchorageFee(entity.getAnchorageFee())
		.securityFee(entity.getSecurityFee())
		.build();
	}
}
