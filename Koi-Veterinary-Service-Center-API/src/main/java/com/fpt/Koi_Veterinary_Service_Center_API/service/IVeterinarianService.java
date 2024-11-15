package com.fpt.Koi_Veterinary_Service_Center_API.service;


import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.createVeterinarianRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.veterinarianRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.veterinarianResponse;

import java.util.List;

public interface IVeterinarianService {
    veterinarianResponse createVeterinarian(createVeterinarianRequest createVeterinarianRequest);

    veterinarianResponse getVeterinarianByID(String travelExpenseID);

    List<veterinarianResponse> getVeterinarian();

    veterinarianResponse updateVeterinarian(veterinarianRequest veterinarianRequest);

    void deleteVeterinarian(String veterinarianID);
}
