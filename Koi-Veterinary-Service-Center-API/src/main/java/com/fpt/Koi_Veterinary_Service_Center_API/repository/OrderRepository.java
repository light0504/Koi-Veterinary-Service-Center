package com.fpt.Koi_Veterinary_Service_Center_API.repository;

import com.fpt.Koi_Veterinary_Service_Center_API.entity.Order;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.Slot;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.User;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.Veterinarian;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {
    Optional<Order> findByOrderID(String orderID);
    List<Order> findByOrderDateAndSlot(LocalDate orderDate, Slot slot);
    List<Order> findByVeterinarianAndOrderDateAndSlot(Veterinarian veterinarian, LocalDate orderDate, Slot slot);
    List<Order> findByVeterinarian(Veterinarian veterinarian);
    List<Order> findByUser(User user);
}
