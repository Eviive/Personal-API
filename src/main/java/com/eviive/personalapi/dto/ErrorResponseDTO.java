package com.eviive.personalapi.dto;

import lombok.Data;

@Data
public class ErrorResponseDTO {

    private Integer status;

    private String error;

    private Object message;

}
