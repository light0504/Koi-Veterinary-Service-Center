package com.fpt.Koi_Veterinary_Service_Center_API.controller;

import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.reportResponse;
import com.fpt.Koi_Veterinary_Service_Center_API.service.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReportController {
    @Autowired
    private IReportService reportService;

    @GetMapping("/report")
    public ResponseEntity<?> getAllReport() {
        reportResponse response = reportService.getAllReport();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
