package com.fpt.Koi_Veterinary_Service_Center_API.controller;

import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.invoiceRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.fishResponse;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.invoiceResponse;
import com.fpt.Koi_Veterinary_Service_Center_API.service.IInvoiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class InvoiceController {

    @Autowired
    private IInvoiceService invoiceService;

//    @PostMapping("/invoices")
//    public ResponseEntity<?> createInvoice(@Valid @RequestBody invoiceRequest invoiceRequest) {
//        invoiceResponse response = invoiceService.createInvoice(invoiceRequest);
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
//    }

    @GetMapping("/invoices")
    public ResponseEntity<?> getAllOrder() {
        List<invoiceResponse> responses = invoiceService.getAllInvoice();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/invoices/{invoiceId}")
    public ResponseEntity<?> getInvoiceById(@PathVariable("invoiceId") String invoiceId) {
        invoiceResponse response = invoiceService.getInvoiceById(invoiceId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/invoices/orders/{orderId}")
    public ResponseEntity<?> getInvoiceByOrderId(@PathVariable("orderId") String orderId) {
        invoiceResponse response = invoiceService.getInvoiceByOrderId(orderId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
