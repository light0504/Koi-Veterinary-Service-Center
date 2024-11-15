package com.fpt.Koi_Veterinary_Service_Center_API.repository;

import com.fpt.Koi_Veterinary_Service_Center_API.entity.Fish;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.FishID;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.Order;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FishRepository extends JpaRepository<Fish, FishID> {
    Boolean existsByFishID(String FishID);
    Optional<Fish> findByFishID(String fishId);
}
