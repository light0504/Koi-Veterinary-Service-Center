package com.fpt.Koi_Veterinary_Service_Center_API.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.OrderDetail;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.enums.OrderStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Data
public class createOrderRequest {
    @NotBlank(message = "UserId cannot be empty")
    private String userID;
//    @NotNull(message = "VeterinaId cannot be empty")
    private String veterinaID;
    @NotNull(message = "day cannot be empty")
    private LocalDate date;
    @NotNull(message = "slot cannot be empty")
    private int slot;
    @NotBlank(message = "Address cannot be empty")
    private String address;
    @NotNull(message = "serviceID is empty")
    private List<orderDetailRequest> services;

}
