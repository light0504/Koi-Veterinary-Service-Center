package com.fpt.Koi_Veterinary_Service_Center_API.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.OrderDetail;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.enums.OrderStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class orderResponse {
    private String orderId;
    private String userId;
    private String veterinaId;
    private LocalDate orderDate;
    private int slot;
    private String address;
    private String description;
    private String travelExpenseId;
    private List<OrderDetailResponse> services;
    private OrderStatus status;
    private String invoiceId;
    private Integer total;
    private LocalDateTime invDate;
    private String method;
}
