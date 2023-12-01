package com.eviive.personalapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponseDTO<E> {

    private Integer status;

    private String error;

    private E message;

}
