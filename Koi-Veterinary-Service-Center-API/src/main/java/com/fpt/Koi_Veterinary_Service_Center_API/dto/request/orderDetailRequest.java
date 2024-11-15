package com.fpt.Koi_Veterinary_Service_Center_API.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class orderDetailRequest {
    @NotBlank(message = "serviceID is empty")
    private String serviceID;
    @NotNull(message = "quantity is empty")
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}
