package com.fpt.Koi_Veterinary_Service_Center_API.service;

import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.loginRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.userRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.loginResponse;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.userResponse;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.User;
import jakarta.validation.Valid;

import java.util.List;

public interface IUserService {
    userResponse register(userRequest userRequest);

    loginResponse login(loginRequest loginRequest);

    userResponse getUserByID(String id);

    List<userResponse> getAllUser();

    List<userResponse> getAllUserByName(String name);

    List<userResponse> getAllUserByRole(String role);

    void deleteUserByID(String id);

    userResponse updateUser(@Valid userRequest userRequest);

    userResponse getLoginUser(String token);
}
