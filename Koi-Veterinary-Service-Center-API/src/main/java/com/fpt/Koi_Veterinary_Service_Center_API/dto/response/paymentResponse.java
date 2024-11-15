package com.fpt.Koi_Veterinary_Service_Center_API.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class paymentResponse {
    private String invoiceId;
    private int total;
    private LocalDateTime invDate;
    private String orderId;
    private String method;
    private String url;
}
