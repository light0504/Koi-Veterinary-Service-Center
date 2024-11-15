package com.fpt.Koi_Veterinary_Service_Center_API.service.impl;

import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.slotRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.slotResponse;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.Slot;
import com.fpt.Koi_Veterinary_Service_Center_API.exception.AppException;
import com.fpt.Koi_Veterinary_Service_Center_API.repository.SlotRepository;
import com.fpt.Koi_Veterinary_Service_Center_API.service.ISlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SlotServiceImpl implements ISlotService {
    @Autowired
    private SlotRepository slotRepository;

    @Override
    public slotResponse getSlotByID(int slotID) {
        Slot slot = slotRepository.findBySlot(slotID).orElseThrow(() -> new AppException("Slot not found"));
        slotResponse response = new slotResponse();
        response.setSlot(slot.getSlot());
        response.setStartTime(slot.getStartTime());
        response.setEndTime(slot.getEndTime());
        return response;
    }

    @Override
    public List<slotResponse> getAllSlot() {
        List<Slot> slots = slotRepository.findAll();
        List<slotResponse> slotResponses = new ArrayList<>();
        for (Slot slot : slots) {
            slotResponse response = new slotResponse();
            response.setSlot(slot.getSlot());
            response.setStartTime(slot.getStartTime());
            response.setEndTime(slot.getEndTime());
            slotResponses.add(response);
        }
        return slotResponses;
    }

    @Override
    public slotResponse updateSlot(slotRequest slotRequest) {
        slotResponse response = new slotResponse();
        Slot slot = slotRepository.findBySlot(slotRequest.getSlot())
                .orElseThrow(() -> new AppException("Slot not found"));
        slot.setEndTime(slotRequest.getEndTime());
        slot.setStartTime(slotRequest.getStartTime());
        Slot savedService = slotRepository.save(slot);
        response.setSlot(savedService.getSlot());
        response.setStartTime(savedService.getStartTime());
        response.setEndTime(savedService.getEndTime());
        return response;
    }
}
