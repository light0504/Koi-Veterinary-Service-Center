package com.fpt.Koi_Veterinary_Service_Center_API.repository;

import com.fpt.Koi_Veterinary_Service_Center_API.entity.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SlotRepository extends JpaRepository<Slot, Integer> {
    Optional<Slot> findBySlot(int slot);

}
