package com.kdigital.SecondProject.dto;

import java.time.LocalDateTime;

import com.kdigital.SecondProject.entity.AISEntity;
import com.kdigital.SecondProject.entity.VoyageEntity;

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
public class AISDTO {
	private Long aisNumber;
	private String callSign;
	private VoyageEntity voyage;
	private LocalDateTime signalDate;
	private double latitude;
	private double longitude;
	private double speed;
	private double direction;
	private String departure;
	private String destination;
	
	
	//entity->dto
	public static AISDTO toDTO(AISEntity entity) {
		return AISDTO.builder()
				.aisNumber(entity.getAisNumber())
				.callSign(entity.getCallSign())
				.voyage(entity.getVoyage())
				.signalDate(entity.getSignalDate())
				.latitude(entity.getLatitude())
				.longitude(entity.getLongitude())
				.speed(entity.getSpeed())
				.direction(entity.getDirection())
				.departure(entity.getDeparture())
				.destination(entity.getDestination())
				.build();
	}
}
