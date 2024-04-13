package com.eviive.personalapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SortUpdateDTO {

    @NotNull(message = "The sort id is required.")
    private Long id;

    @NotNull(message = "The sort value is required.")
    @Min(value = 0, message = "The sort value cannot be less than {min}.")
    private Integer sort;

}
