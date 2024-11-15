package com.fpt.Koi_Veterinary_Service_Center_API.dto.request;

import com.fpt.Koi_Veterinary_Service_Center_API.entity.enums.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class orderStatusRequest {
    @NotNull(message = "status cannot be empty")
    private OrderStatus status;
}
