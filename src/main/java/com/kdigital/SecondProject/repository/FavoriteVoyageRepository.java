package com.kdigital.SecondProject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kdigital.SecondProject.entity.FavoriteVoyageEntity;
import com.kdigital.SecondProject.entity.UserEntity;

public interface FavoriteVoyageRepository extends JpaRepository<FavoriteVoyageEntity, Long> {

	Optional<FavoriteVoyageEntity> findByUserEntityAndTopFavorite(UserEntity userEntity, String string);

	Optional<FavoriteVoyageEntity> findByUserIdAndVNumber(String userId, int vNumber);

}
