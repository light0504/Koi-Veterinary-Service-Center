package com.fpt.Koi_Veterinary_Service_Center_API.controller;

import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.slotRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.slotResponse;
import com.fpt.Koi_Veterinary_Service_Center_API.service.ISlotService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SlotController {
    @Autowired
    private ISlotService slotService;

    @GetMapping("/slot/{slotID}")
    public ResponseEntity<?> getSlotByID(@PathVariable("slotID") int slotID) {
        slotResponse response = slotService.getSlotByID(slotID);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/slots")
    public ResponseEntity<?> getAllSlot() {
        List<slotResponse> slots = slotService.getAllSlot();
        return new ResponseEntity<>(slots, HttpStatus.OK);
    }

    @PutMapping("/slot/update")
    public ResponseEntity<?> updateSlot(@Valid @RequestBody slotRequest slotRequest) {
        slotResponse response = slotService.updateSlot(slotRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
