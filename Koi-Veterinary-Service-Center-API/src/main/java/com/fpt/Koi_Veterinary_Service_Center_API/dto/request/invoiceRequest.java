package com.fpt.Koi_Veterinary_Service_Center_API.dto.request;

import com.fpt.Koi_Veterinary_Service_Center_API.entity.Order;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class invoiceRequest {

    @NotBlank(message = "OrderId is Empty")
    private String orderId;
}
