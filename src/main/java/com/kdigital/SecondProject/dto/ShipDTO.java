package com.kdigital.SecondProject.dto;

import com.kdigital.SecondProject.entity.ShipEntity;

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
public class ShipDTO {
	private String callSign; 	// 선박 콜사인 (PK)
	private String shipName; 	// 선박명
	private int shipType; 		// 선박 종류 (1: 컨테이너)
	private int tonnage;		// 선박 무게
	private String mmsi;		// 선박의 mmsi
	private String imo;			// 선박의 imo
	
	//Entity를 받아 DTO로 반환
	public static ShipDTO toDTO(ShipEntity shipentity) {
		return ShipDTO.builder()
				.callSign(shipentity.getCallSign())
				.shipName(shipentity.getShipName())
				.shipType(shipentity.getShipType())
				.tonnage(shipentity.getTonnage())
				.mmsi(shipentity.getMmsi())
				.imo(shipentity.getImo())
				.build();
	}
}