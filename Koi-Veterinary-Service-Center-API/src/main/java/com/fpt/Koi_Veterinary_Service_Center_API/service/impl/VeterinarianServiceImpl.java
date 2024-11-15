package com.fpt.Koi_Veterinary_Service_Center_API.service.impl;

import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.createVeterinarianRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.veterinarianRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.veterinarianResponse;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.Role;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.User;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.Veterinarian;
import com.fpt.Koi_Veterinary_Service_Center_API.exception.AppException;
import com.fpt.Koi_Veterinary_Service_Center_API.repository.RoleRepository;
import com.fpt.Koi_Veterinary_Service_Center_API.repository.UserRepository;
import com.fpt.Koi_Veterinary_Service_Center_API.repository.VeterinarianRepository;
import com.fpt.Koi_Veterinary_Service_Center_API.service.IVeterinarianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Service
public class VeterinarianServiceImpl implements IVeterinarianService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VeterinarianRepository veterinarianRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public veterinarianResponse createVeterinarian(createVeterinarianRequest createVeterinarianRequest) {
        User user = userRepository.findByUserID(createVeterinarianRequest.getUserID()).orElseThrow(()-> new AppException("User not found"));
        if(user.getRole().getRoleID().equals("V")){
            throw new AppException("User already veterinarian");
        }
        Role role = new Role();
        role.setRoleID("V");
        role.setTitle("Veterina");
        user.setRole(role);
        Veterinarian veterinarian = new Veterinarian();
        veterinarian.setUser(user);
        veterinarian.setDescription(createVeterinarianRequest.getDescription());
        veterinarian.setStatus(TRUE);
        Veterinarian savedVeterinarian = veterinarianRepository.save(veterinarian);
        veterinarianResponse response = new veterinarianResponse();
        response.setDescription(savedVeterinarian.getDescription());
        response.setStatus(savedVeterinarian.getStatus());
        response.setVeterinaID(savedVeterinarian.getVeterinarianID());
        response.setUserID(savedVeterinarian.getUser().getUserID());
        response.setName(userRepository.findByUserID(savedVeterinarian.getUser().getUserID()).orElseThrow(()-> new AppException("User not found")).getName());
        return response;
    }

    @Override
    public veterinarianResponse getVeterinarianByID(String veterinarianID) {
        Veterinarian veterinarian = veterinarianRepository.findByVeterinarianID(veterinarianID).orElseThrow(() -> new AppException("veterinarian not found"));
        veterinarianResponse response = new veterinarianResponse();
        response.setUserID(veterinarian.getVeterinarianID());
        response.setStatus(veterinarian.getStatus());
        response.setDescription(veterinarian.getDescription());
        response.setVeterinaID(veterinarian.getVeterinarianID());
        response.setName(userRepository.findByUserID(veterinarian.getUser().getUserID()).orElseThrow(()-> new AppException("User not found")).getName());
        return response;
    }

    @Override
    public List<veterinarianResponse> getVeterinarian() {
        List<Veterinarian> veterinarians = veterinarianRepository.findAll();
        List<veterinarianResponse> veterinarianResponses = new ArrayList<>();
        for (Veterinarian veterinarian : veterinarians) {
            veterinarianResponse response = new veterinarianResponse();
            response.setVeterinaID(veterinarian.getVeterinarianID());
            response.setStatus(veterinarian.getStatus());
            response.setDescription(veterinarian.getDescription());
            response.setUserID(veterinarian.getUser().getUserID());
            response.setName(userRepository.findByUserID(veterinarian.getUser().getUserID()).orElseThrow(()-> new AppException("User not found")).getName());
            veterinarianResponses.add(response);
        }
        return veterinarianResponses;
    }

    @Override
    public veterinarianResponse updateVeterinarian(veterinarianRequest veterinarianRequest) {
        Veterinarian veterinarian = veterinarianRepository.findByVeterinarianID(veterinarianRequest.getVeterinaID())
                .orElseThrow(() -> new AppException("Veterinarian not found"));
        veterinarian.setDescription(veterinarianRequest.getDescription());
        Veterinarian savedVeterinarian = veterinarianRepository.save(veterinarian);
        veterinarianResponse response = new veterinarianResponse();
        response.setUserID(savedVeterinarian.getUser().getUserID());
        response.setDescription(savedVeterinarian.getDescription());
        response.setVeterinaID(savedVeterinarian.getVeterinarianID());
        response.setStatus(savedVeterinarian.getStatus());
        response.setName(userRepository.findByUserID(savedVeterinarian.getUser().getUserID()).orElseThrow(()-> new AppException("User not found")).getName());
        return response;
    }

    @Override
    public void deleteVeterinarian(String veterinarianID) {
        Veterinarian veterinarian = veterinarianRepository.findByVeterinarianID(veterinarianID)
                .orElseThrow(() -> new AppException("Veterinarian not found"));
        veterinarian.setStatus(FALSE);
        veterinarianRepository.save(veterinarian);
    }
}
