package com.kdigital.SecondProject.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kdigital.SecondProject.entity.AISEntity;

public interface AISRepository extends JpaRepository<AISEntity, Long> {
	// vNumber로 검색
	List<AISEntity> findByVoyage_vNumber(Long vNumber);  // VoyageEntity의 vNumber를 기반으로 검색
	

	
	@Query("SELECT a FROM AISEntity a " +
		       "JOIN a.voyage v " +
		       "WHERE v.vNumber = :vNumber " +
		       "AND DATEDIFF(CURRENT_DATE, a.signalDate) >= 730 " +
		       "ORDER BY a.signalDate ASC")
		List<AISEntity> findVoyageWithCallSignAsAISEntity(@Param("vNumber") Long vNumber);

	//@Query("SELECT a FROM AisEntity a WHERE a.vNumber = :vNumber AND a.signalDate <= :targetDate ORDER BY a.signalDate DESC")
    //List<AISEntity> findClosestPastRecordsByVNumber(@Param("vNumber") Integer vNumber, @Param("targetDate") LocalDateTime targetDate, Pageable pageable);

	Optional<AISEntity> findFirstByVoyage_vNumberAndSignalDateBeforeOrderBySignalDateDesc(Long vNumber, LocalDateTime signalDate);


}
