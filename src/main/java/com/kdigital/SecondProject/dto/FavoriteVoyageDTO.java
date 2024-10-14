package com.kdigital.SecondProject.dto;
import com.kdigital.SecondProject.entity.FavoriteVoyageEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class FavoriteVoyageDTO {
    private Long fsNumber;
    private String userId;  // UserEntity의 userId를 사용
    private int vNumber;
    private String topFavorite;

    // Entity에서 DTO로 변환하는 메소드
    public static FavoriteVoyageDTO toDTO(FavoriteVoyageEntity favoriteVoyageEntity, String userId, int vNumber) {
        return FavoriteVoyageDTO.builder()
        		.fsNumber(favoriteVoyageEntity.getFsNumber())
                .userId(userId) // 엔티티에서 userId 가져오기
                .vNumber(vNumber)
                .topFavorite(favoriteVoyageEntity.getTopFavorite())
                .build();
    }
}
