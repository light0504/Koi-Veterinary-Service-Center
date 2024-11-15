package com.fpt.Koi_Veterinary_Service_Center_API.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class orderDescriptionRequest {
    @NotBlank(message = "Description cannot be empty")
    private String description;
}
