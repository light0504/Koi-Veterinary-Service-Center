package com.fpt.Koi_Veterinary_Service_Center_API.controller;

import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.createVeterinarianRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.veterinarianRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.veterinarianResponse;
import com.fpt.Koi_Veterinary_Service_Center_API.service.IVeterinarianService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class VeterinarianController {
    @Autowired
    private IVeterinarianService veterinarianService;
    @PostMapping("/veterina/add")
    public ResponseEntity<?> createVeterinarian(@Valid @RequestBody createVeterinarianRequest createVeterinarianRequest) {
        veterinarianResponse response = veterinarianService.createVeterinarian(createVeterinarianRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/veterinas")
    public ResponseEntity<?> getVeterinarian() {
        List<veterinarianResponse> veterinarianResponses = veterinarianService.getVeterinarian();
        return new ResponseEntity<>(veterinarianResponses, HttpStatus.OK);
    }

    @GetMapping("/veterina/{veterinarianID}")
    public ResponseEntity<?> getVeterinarianByID(@PathVariable("veterinarianID") String veterinarianID) {
        veterinarianResponse response = veterinarianService.getVeterinarianByID(veterinarianID);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/veterina/update")
    public ResponseEntity<?> updateVeterinarian(@Valid @RequestBody veterinarianRequest veterinarianRequest) {
        veterinarianResponse response = veterinarianService.updateVeterinarian(veterinarianRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/veterina/delete/{veterinarianID}")
    public ResponseEntity<?> deleteVeterinarian(@PathVariable("veterinarianID") String veterinarianID) {
        veterinarianService.deleteVeterinarian(veterinarianID);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }
}
