package com.kdigital.SecondProject.entity;

import com.kdigital.SecondProject.dto.FavoriteVoyageDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Setter
@Getter
@ToString
@Builder
@Entity
@Table(name = "favorite_voyage")
public class FavoriteVoyageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fs_number")
    private Long fsNumber;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;  // UserEntity 객체를 사용

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "v_number", nullable = false)
    private VoyageEntity voyageEntity;

    @Column(name = "top_favorite")
    private String topFavorite;

    // DTO에서 엔티티로 변환하는 메소드
    public static FavoriteVoyageEntity toEntity(FavoriteVoyageDTO favoriteVoyageDTO) {
        return FavoriteVoyageEntity.builder()
                .fsNumber(favoriteVoyageDTO.getFsNumber())
                .userEntity(favoriteVoyageDTO.getUserEntity())  // 인자로 받은 UserEntity 객체 사용
                .voyageEntity(favoriteVoyageDTO.getVoyageEntity())
                .topFavorite(favoriteVoyageDTO.getTopFavorite())
                .build();
    }
}
