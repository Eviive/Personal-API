package com.eviive.personalapi.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class RevalidateResponseDTO {

    private Boolean revalidated;

    private Timestamp timestamp;

}
