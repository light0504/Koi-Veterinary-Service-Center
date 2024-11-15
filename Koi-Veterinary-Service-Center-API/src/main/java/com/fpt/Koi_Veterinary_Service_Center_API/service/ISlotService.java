package com.fpt.Koi_Veterinary_Service_Center_API.service;

import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.slotRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.slotResponse;

import java.util.List;

public interface ISlotService {
    slotResponse getSlotByID(int serviceID);

    List<slotResponse> getAllSlot();

    slotResponse updateSlot(slotRequest slotRequest);
}
