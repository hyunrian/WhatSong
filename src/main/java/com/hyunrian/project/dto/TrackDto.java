package com.hyunrian.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TrackDto {
    private String id;
    private String name;
    private String trackNumber;
}
