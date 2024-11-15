package com.fpt.Koi_Veterinary_Service_Center_API.dto.request;

import com.fpt.Koi_Veterinary_Service_Center_API.entity.Invoice;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class feedbackRequest {
    @NotBlank(message = "comment cannot be empty")
    private String comment;
    @NotNull(message = "rating cannot be empty")
    @Min(value = 0, message = "rating from 0-5")
    @Max(value = 5, message = "rating from 0-5")
    private Float rating;
}
