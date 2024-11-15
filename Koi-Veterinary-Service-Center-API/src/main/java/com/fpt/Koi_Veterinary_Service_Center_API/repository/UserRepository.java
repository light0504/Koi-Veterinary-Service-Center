package com.fpt.Koi_Veterinary_Service_Center_API.repository;

import com.fpt.Koi_Veterinary_Service_Center_API.entity.Role;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserID(String userID);
    Boolean existsByUserID(String userID);
    List<User> findAllByNameContainingIgnoreCase(String name);
    List<User> findAllByRole(Role role);
    void deleteByUserID(String id);
}
