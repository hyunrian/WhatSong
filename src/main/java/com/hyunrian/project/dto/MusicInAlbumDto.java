package com.hyunrian.project.dto;

import com.hyunrian.project.domain.Album;
import com.hyunrian.project.domain.enums.BrandName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class MusicInAlbumDto {
    private Album album;
    private BrandName brandName;
}
