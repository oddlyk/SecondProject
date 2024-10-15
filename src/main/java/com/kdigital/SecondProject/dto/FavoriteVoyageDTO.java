package com.kdigital.SecondProject.dto;
import com.kdigital.SecondProject.entity.FavoriteVoyageEntity;
import com.kdigital.SecondProject.entity.UserEntity;
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
@Builder
@ToString
public class FavoriteVoyageDTO {
    private Long fsNumber;
    private UserEntity userEntity; 
    private VoyageEntity voyageEntity;
    private String topFavorite;

    // Entity에서 DTO로 변환하는 메소드
    public static FavoriteVoyageDTO toDTO(FavoriteVoyageEntity favoriteVoyageEntity) {
        return FavoriteVoyageDTO.builder()
        		.fsNumber(favoriteVoyageEntity.getFsNumber())
                .userEntity(favoriteVoyageEntity.getUserEntity()) // 엔티티에서 userId 가져오기
                .voyageEntity(favoriteVoyageEntity.getVoyageEntity())
                .topFavorite(favoriteVoyageEntity.getTopFavorite())
                .build();
    }
}
