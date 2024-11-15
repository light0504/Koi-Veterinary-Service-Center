package com.fpt.Koi_Veterinary_Service_Center_API.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class reportResponse {
    private int totalOrders;
    private int totalCustomers;
    private long totalPayments;
    private String bestService;
}
