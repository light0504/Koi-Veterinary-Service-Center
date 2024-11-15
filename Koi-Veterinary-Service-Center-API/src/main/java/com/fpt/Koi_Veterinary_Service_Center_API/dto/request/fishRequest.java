package com.fpt.Koi_Veterinary_Service_Center_API.dto.request;

import com.fpt.Koi_Veterinary_Service_Center_API.entity.User;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class fishRequest {
    @NotNull(message = "weight is Empty")
    @Min(value = 1, message = "weight must be more than 0")
    private Float weight;
    @NotNull(message = "length is Empty")
    @Min(value = 1, message = "lenght must be more than 0")
    private Float length;
    @NotNull(message = "month is Empty")
    @Min(value = 1, message = "minimum month 1")
    private int month;
    @NotBlank(message = "describe is Empty")
    private String describe;
    @NotBlank(message = "userId is Empty")
    private String userID;
}
