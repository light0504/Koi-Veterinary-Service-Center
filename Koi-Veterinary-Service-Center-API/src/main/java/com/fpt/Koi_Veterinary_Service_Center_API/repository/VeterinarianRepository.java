package com.fpt.Koi_Veterinary_Service_Center_API.repository;

import com.fpt.Koi_Veterinary_Service_Center_API.entity.Service;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.User;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.Veterinarian;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VeterinarianRepository extends JpaRepository<Veterinarian, String> {
    Optional<Veterinarian> findByUser(User user);
    Boolean existsByVeterinarianID(String veterinarianID);
    Optional<Veterinarian> findByVeterinarianID(String veterinarianID);
}
