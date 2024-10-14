package com.kdigital.SecondProject.entity;

import java.time.LocalDateTime;

import com.kdigital.SecondProject.dto.AISDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
	
    @Column(name="call_sign") 
	private String callSign;  // Voyage 테이블의 call_sign 참조 (외래키)
	
    @Column(name="v_number")
	private Long vNumber;   // Voyage 테이블의 v_number 참조 (외래키)
	
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
	
	@Column(name="destination")
	private String destination;  // Voyage 테이블의 destination 참조 (외래키)
	
	
	
	@ManyToOne
	@JoinColumn(name = "v_number", referencedColumnName = "v_number", insertable = false, updatable = false)
	private VoyageEntity voyage; // VoyageEntity와의 관계 추가

	
	// 위의 private VoyageEntity voyage를 제외한 생성자 만들기
    public AISEntity(Long aisNumber, String callSign, Long vNumber, LocalDateTime signalDate, 
                     Double latitude, Double longitude, Double speed, Double direction, 
                     String departure, String destination) {
        this.aisNumber = aisNumber;
        this.callSign = callSign;
        this.vNumber = vNumber;
        this.signalDate = signalDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.direction = direction;
        this.departure = departure;
        this.destination = destination;
    }

	
	//dto->entity
	public static AISEntity toEntity(AISDTO dto) {
		return AISEntity.builder()
				.aisNumber(dto.getAisNumber())
				.callSign(dto.getCallSign())
				.vNumber(dto.getVNumber())
				.signalDate(dto.getSignalDate())
				.latitude(dto.getLatitude())
				.longitude(dto.getLongitude())
				.speed(dto.getSpeed())
				.direction(dto.getDirection())
				.departure(dto.getDeparture())
				.destination(dto.getDestination()) //.voyage2(entity.getVoyage2())
				.build();
	}
}
