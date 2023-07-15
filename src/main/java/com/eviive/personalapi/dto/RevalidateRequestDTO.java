package com.eviive.personalapi.dto;

import lombok.Data;

@Data
public class RevalidateRequestDTO {

    private String secret;

    private String path;

    public static RevalidateRequestDTO of(String secret, String path) {
        RevalidateRequestDTO revalidateRequestDTO = new RevalidateRequestDTO();
        revalidateRequestDTO.setPath(path);
        revalidateRequestDTO.setSecret(secret);
        return revalidateRequestDTO;
    }

    public static RevalidateRequestDTO of(String secret) {
        return of(secret, null);
    }

}
