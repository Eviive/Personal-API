package com.eviive.personalapi.dto;

import lombok.Data;

@Data
public class ImageDTO implements IDTO {

    private Long id;

    private String url;

    private String alt;

}
