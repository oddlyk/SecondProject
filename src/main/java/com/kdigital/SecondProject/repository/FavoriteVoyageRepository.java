package com.kdigital.SecondProject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kdigital.SecondProject.entity.FavoriteVoyageEntity;

public interface FavoriteVoyageRepository extends JpaRepository<FavoriteVoyageEntity, Long> {

	Optional<FavoriteVoyageEntity> findByTopFavorite(String string);

}
