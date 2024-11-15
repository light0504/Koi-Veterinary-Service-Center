package com.fpt.Koi_Veterinary_Service_Center_API.controller;

import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.feedbackRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.fishRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.feedbackResponse;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.fishResponse;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.serviceResponse;
import com.fpt.Koi_Veterinary_Service_Center_API.service.IFishService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FishController {
    @Autowired
    private IFishService fishService;

    @GetMapping("/fish")
    public ResponseEntity<?> getAllFish() {
        List<fishResponse> fishes = fishService.getAllFish();
        return new ResponseEntity<>(fishes, HttpStatus.OK);
    }

    @GetMapping("/fish/{userId}")
    public ResponseEntity<?> getFishByUserId(@PathVariable("userId") String userId) {
        List<fishResponse> fishes = fishService.getFishByUserId(userId);
        return new ResponseEntity<>(fishes, HttpStatus.OK);
    }

    @PostMapping("/fish")
    public ResponseEntity<?> createFish(@Valid @RequestBody fishRequest fishRequest) {
        fishResponse response = fishService.createFish(fishRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/fish/{fishId}")
    public ResponseEntity<?> updateFish(@PathVariable("fishId") String fishId, @Valid @RequestBody fishRequest fishRequest) {
        fishResponse response = fishService.updateFish(fishId, fishRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/fish/{fishId}")
    public ResponseEntity<?> deleteFish(@PathVariable("fishId") String fishId) {
        fishService.deleteFish(fishId);
        return new ResponseEntity<>("Deleted", HttpStatus.CREATED);
    }
}
