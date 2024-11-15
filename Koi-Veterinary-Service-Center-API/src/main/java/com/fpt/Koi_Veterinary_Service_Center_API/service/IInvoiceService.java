package com.fpt.Koi_Veterinary_Service_Center_API.service;

import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.invoiceRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.invoiceResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface IInvoiceService {
    invoiceResponse createInvoice(invoiceRequest invoiceRequest);

    List<invoiceResponse> getAllInvoice();

    invoiceResponse getInvoiceById(String invoiceId);

    invoiceResponse getInvoiceByOrderId(String orderId);
}
