package com.hyunrian.project.dto.music;

import com.hyunrian.project.domain.enums.music.SearchType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MusicSearchCondition {

    private SearchType searchType;
    private String keyword;
//    private BrandName brandName;

}
