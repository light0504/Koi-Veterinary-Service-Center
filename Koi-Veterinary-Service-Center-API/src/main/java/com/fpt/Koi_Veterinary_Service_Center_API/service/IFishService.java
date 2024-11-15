package com.fpt.Koi_Veterinary_Service_Center_API.service;

import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.fishRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.fishResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface IFishService {
    List<fishResponse> getAllFish();

    fishResponse createFish(@Valid fishRequest fishRequest);

    List<fishResponse> getFishByUserId(String userId);

    fishResponse updateFish(String fishId, @Valid fishRequest fishRequest);

    void deleteFish(String fishId);
}
