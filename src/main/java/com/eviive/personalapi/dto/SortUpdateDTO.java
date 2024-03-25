package com.eviive.personalapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SortUpdateDTO {

    @NotNull(message = "The id cannot be null.")
    private Long id;

    @NotNull(message = "The sort cannot be null.")
    @Min(value = 0, message = "The sort cannot be less than {min}.")
    private Integer sort;

}
