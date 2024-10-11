package com.kdigital.SecondProject.entity;

import com.kdigital.SecondProject.dto.ShipDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString

@Entity
@Table(name="ship")
public class ShipEntity {
	@Id
	@Column(name="call_sign")
	private String callSign; 	// 선박 콜사인 (PK)
	
	@Column(name="ship_name")
	private String shipName; 	// 선박명
	
	@Column(name="ship_type")
	private int shipType; 		// 선박 종류 (1: 컨테이너)
	
	@Column(name="tonnage")
	private int tonnage;		// 선박 무게
	
	@Column(name="mmsi")
	private String mmsi;		// 선박의 mmsi
	
	@Column(name="imo")
	private String imo;			// 선박의 imo
	
	//dto->entity
	public static ShipEntity toEntity(ShipDTO shipdto) {
		return ShipEntity.builder()
				.callSign(shipdto.getCallSign())
				.shipName(shipdto.getShipName())
				.shipType(shipdto.getShipType())
				.tonnage(shipdto.getTonnage())
				.mmsi(shipdto.getMmsi())
				.imo(shipdto.getImo())
				.build();
	}
}
