package com.kdigital.SecondProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kdigital.SecondProject.entity.VoyageEntity;

public interface VoyageRepository extends JpaRepository<VoyageEntity, Long> {
	
	// 아래에 쓰여지는 함수명은 JPA의 규칙을 따른 것
	
	// callSign으로 검색
	// List 내에는 아래와 같이 들어있음 : VoyageDTO(vNumber=1, ship=ShipEntity(callSign=C6TI4, shipName=MARVEL, shipType=1, tonnage=5403, mmsi=311655000, imo=9296456.0), departureDate=2022-09-01T03:45:07,생략)
	List<VoyageEntity> findByShip_CallSign(String callSign);  // ShipEntity의 callSign을 기반으로 검색
					//외래키 참조 형을 사용하지 않으면 findBy변수명 으로 함수명을 생성 : findByCallSign
					// 외래키 참조 형을 사용했다면 findBy변수명(entity변수)_참조한entity의 찾고자 하는 컬럼명 으로 함수명 생성 : findByShip_CallSign
	
	// mmsi로 검색
	List<VoyageEntity> findByShip_Mmsi(String mmsi);  // ShipEntity의 mmsi를 기반으로 검색
	
	// imo로 검색
	List<VoyageEntity> findByShip_Imo(String imo);  // ShipEntity의 imo를 기반으로 검색

	
	@Query("SELECT v FROM VoyageEntity v JOIN FETCH v.ship s WHERE s.callSign = :callSign")
	List<VoyageEntity> findVoyageWithShipByCallSign(@Param("callSign") String callSign);

	
}
