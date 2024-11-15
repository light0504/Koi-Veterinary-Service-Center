package com.fpt.Koi_Veterinary_Service_Center_API.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class createServiceRequest {
    @NotBlank(message = "Service name cannot be empty")
    private String Name;

    @NotBlank(message = "Service type cannot be empty") // Optional validation for type
    private String Type;

    @NotNull(message = "Price cannot be null")
    @Min(value = 1, message = "price must be at least 1")
    private Float price;

    @NotNull(message = "maxQuantity cannot be null")
    @Min(value = 1, message = "maxQuantity must be at least 1")
    private int maxQuantity;

    @NotNull(message = "isService cannot be null")
    private boolean isService;
}
