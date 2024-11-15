package com.fpt.Koi_Veterinary_Service_Center_API.service.impl;

import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.createServiceRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.serviceRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.serviceResponse;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.Service;
import com.fpt.Koi_Veterinary_Service_Center_API.exception.AppException;
import com.fpt.Koi_Veterinary_Service_Center_API.repository.ServiceRepository;
import com.fpt.Koi_Veterinary_Service_Center_API.service.IServiceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class ServiceServiceImpl implements IServiceService {
    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public serviceResponse createService(createServiceRequest serviceRequest) {
        serviceResponse response = new serviceResponse();
        Service service = new Service();
        service.setName(serviceRequest.getName());
        service.setType(serviceRequest.getType());
        service.setPrice(serviceRequest.getPrice());
        service.setMaxQuantity(serviceRequest.getMaxQuantity());
        service.setService(serviceRequest.isService());
        Service savedService = serviceRepository.save(service);
        response.setServiceID(savedService.getServiceID());
        response.setName(savedService.getName());
        response.setType(savedService.getType());
        response.setPrice(savedService.getPrice());
        response.setMaxQuantity(savedService.getMaxQuantity());
        response.setService(savedService.isService());
        return response;
    }

    @Override
    public serviceResponse getServiceByServiceID(String serviceID) {
        Service service = serviceRepository.findByServiceID(serviceID).orElseThrow(() -> new AppException("Service not found"));
        serviceResponse response = new serviceResponse();
        response.setServiceID(service.getServiceID());
        response.setName(service.getName());
        response.setType(service.getType());
        response.setPrice(service.getPrice());
        response.setMaxQuantity(service.getMaxQuantity());
        response.setService(service.isService());
        return response;
    }

    @Override
    public List<serviceResponse> getAllServices() {
        List<Service> services = serviceRepository.findAll();
        List<serviceResponse> serviceResponses = new ArrayList<>();
        for (Service service : services) {
            serviceResponse response = new serviceResponse();
            response.setServiceID(service.getServiceID());
            response.setName(service.getName());
            response.setType(service.getType());
            response.setPrice(service.getPrice());
            response.setMaxQuantity(service.getMaxQuantity());
            response.setService(service.isService());
            serviceResponses.add(response);
        }
        return serviceResponses;
    }

    @Override
    public serviceResponse updateService(serviceRequest serviceRequest) {
        serviceResponse response = new serviceResponse();
        Service service = serviceRepository.findByServiceID(serviceRequest.getServiceID())
                .orElseThrow(() -> new AppException("Service not found"));
        service.setName(serviceRequest.getName());
        service.setType(serviceRequest.getType());
        service.setPrice(serviceRequest.getPrice());
        service.setMaxQuantity(serviceRequest.getMaxQuantity());
        service.setService(serviceRequest.isService());
        Service savedService = serviceRepository.save(service);
        response.setServiceID(savedService.getServiceID());
        response.setName(savedService.getName());
        response.setType(savedService.getType());
        response.setPrice(savedService.getPrice());
        response.setMaxQuantity(savedService.getMaxQuantity());
        response.setService(savedService.isService());
        return response;
    }

    @Override
    @Transactional
    public void deleteService(String serviceID) {
        Service service = serviceRepository.findByServiceID(serviceID)
                .orElseThrow(() -> new AppException("Service not found"));
        serviceRepository.delete(service);
    }
}
