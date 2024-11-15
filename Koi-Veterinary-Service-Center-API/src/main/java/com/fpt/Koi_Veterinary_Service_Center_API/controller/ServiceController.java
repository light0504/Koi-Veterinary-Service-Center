package com.fpt.Koi_Veterinary_Service_Center_API.controller;

import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.createServiceRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.serviceRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.serviceResponse;
import com.fpt.Koi_Veterinary_Service_Center_API.service.IServiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ServiceController {
    @Autowired
    private IServiceService serviceService;
    @PostMapping("/service/add")
    public ResponseEntity<?> addService(@Valid @RequestBody createServiceRequest serviceRequest) {
        serviceResponse response = serviceService.createService(serviceRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/services")
    public ResponseEntity<?> getAllServices() {
        List<serviceResponse> services = serviceService.getAllServices();
        return new ResponseEntity<>(services, HttpStatus.OK);
    }

    @GetMapping("/service/{serviceID}")
    public ResponseEntity<?> getServiceByServiceID(@PathVariable("serviceID") String serviceID) {
        serviceResponse response = serviceService.getServiceByServiceID(serviceID);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/service/update")
    public ResponseEntity<?> updateService(@Valid @RequestBody serviceRequest serviceRequest) {
        serviceResponse response = serviceService.updateService(serviceRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/service/{serviceID}")
    public ResponseEntity<?> deleteService(@PathVariable("serviceID") String serviceID) {
        serviceService.deleteService(serviceID);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }
}
