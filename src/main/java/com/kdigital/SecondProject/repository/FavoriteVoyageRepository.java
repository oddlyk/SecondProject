package com.kdigital.SecondProject.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kdigital.SecondProject.entity.FavoriteVoyageEntity;
import com.kdigital.SecondProject.entity.UserEntity;

public interface FavoriteVoyageRepository extends JpaRepository<FavoriteVoyageEntity, Long> {

	/*즐겨 찾기된 항해 조회*/
	Optional<FavoriteVoyageEntity> findByUserEntityAndTopFavorite(UserEntity userEntity, String string);
	
	/*선호 항해 조회: 사용자 아이디, 항해 번호 */
	Optional<FavoriteVoyageEntity> findByUserEntity_UserIdAndVoyageEntity_vNumber(String userId, Long vNumber);

	/*사용자를 기준으로 선호 항해 전체 조회*/
	List<FavoriteVoyageEntity> findAllByUserEntity_UserId(String userId);

	// 사용자가 등록한 선호 항해 전체 개수를 조회하는 메서드
    long countByUserEntity_UserId(String userId);

    /* top_favorite가 1인 항해를 최상단에 두고, 나머지를 fs_number로 오름차순 정렬하여 조회하는 메서드 */
    @Query("SELECT f FROM FavoriteVoyageEntity f WHERE f.userEntity.userId = :userId ORDER BY CASE WHEN f.topFavorite = '1' THEN 0 ELSE 1 END, f.fsNumber ASC")
    List<FavoriteVoyageEntity> findAllByUserIdSortedByTopFavoriteAndFsNumber(@Param("userId") String userId);
}
