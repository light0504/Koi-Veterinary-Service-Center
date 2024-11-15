package com.fpt.Koi_Veterinary_Service_Center_API.repository;

import com.fpt.Koi_Veterinary_Service_Center_API.entity.Service;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.TravelExpense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TravelExpenseRepository extends JpaRepository<TravelExpense, String> {
    Optional<TravelExpense> findByExpenseID(String travelExpenseID);
    Boolean existsByExpenseID(String travelExpenseID);
    Optional<TravelExpense> findByEndLocation(String endLocation);

}
