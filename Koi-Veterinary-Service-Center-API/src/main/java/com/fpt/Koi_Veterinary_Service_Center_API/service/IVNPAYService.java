package com.fpt.Koi_Veterinary_Service_Center_API.service;

import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.invoiceResponse;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.paymentResponse;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.enums.OrderStatus;
import org.springframework.http.ResponseEntity;

public interface IVNPAYService {
    ResponseEntity<String> payment(String orderId, OrderStatus status);

    paymentResponse paymentSuccess(String responseCode, String Total, String orderInfo);
}
