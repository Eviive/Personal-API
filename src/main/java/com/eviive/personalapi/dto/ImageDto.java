package com.eviive.personalapi.dto;

import lombok.Data;

@Data
public class ImageDto implements IDto {

    private Long id;

    private String url;

    private String alt;

}
