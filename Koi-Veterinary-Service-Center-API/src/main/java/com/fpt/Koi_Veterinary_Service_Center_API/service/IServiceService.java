package com.fpt.Koi_Veterinary_Service_Center_API.service;


import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.createServiceRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.serviceRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.serviceResponse;

import java.util.List;

public interface IServiceService {
    serviceResponse createService(createServiceRequest serviceRequest);

    serviceResponse getServiceByServiceID(String serviceID);

    List<serviceResponse> getAllServices();

    serviceResponse updateService(serviceRequest serviceRequest);

    void deleteService(String serviceID);
}
