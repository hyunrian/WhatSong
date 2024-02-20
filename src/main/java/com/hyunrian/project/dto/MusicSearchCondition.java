package com.hyunrian.project.dto;

import com.hyunrian.project.domain.enums.BrandName;
import com.hyunrian.project.domain.enums.SearchType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MusicSearchCondition {

    private SearchType searchType;
    private String keyword;
//    private BrandName brandName;

}
