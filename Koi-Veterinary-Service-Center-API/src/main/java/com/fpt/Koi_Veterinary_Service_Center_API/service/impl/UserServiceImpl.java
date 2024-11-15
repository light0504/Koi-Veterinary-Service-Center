package com.fpt.Koi_Veterinary_Service_Center_API.service.impl;

import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.loginRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.userRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.loginResponse;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.userResponse;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.Role;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.User;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.Veterinarian;
import com.fpt.Koi_Veterinary_Service_Center_API.exception.AppException;
import com.fpt.Koi_Veterinary_Service_Center_API.repository.RoleRepository;
import com.fpt.Koi_Veterinary_Service_Center_API.repository.UserRepository;
import com.fpt.Koi_Veterinary_Service_Center_API.repository.VeterinarianRepository;
import com.fpt.Koi_Veterinary_Service_Center_API.service.IUserService;
import com.fpt.Koi_Veterinary_Service_Center_API.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private VeterinarianRepository veterinarianRepository;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public userResponse register(userRequest userRequest) {
        userResponse response = new userResponse();
        User user = new User();
        if (userRepository.existsByUserID(userRequest.getUserID())) {
            throw new AppException(userRequest.getUserID() + " already exists");
        } else {
            user.setUserID(userRequest.getUserID());
        }
        Role role = roleRepository.findByTitle("CUSTOMER").orElseThrow(() -> new AppException("Role not found"));
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setAddress(userRequest.getAddress());
        user.setName(userRequest.getName());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setEmail(userRequest.getEmail());
        User savedUser = userRepository.save(user);
        response.setUserID(savedUser.getUserID());
        response.setRole(savedUser.getRole().getRoleID());
        response.setPassword(savedUser.getPassword());
        response.setAddress(savedUser.getAddress());
        response.setName(savedUser.getName());
        response.setPhoneNumber(savedUser.getPhoneNumber());
        response.setEmail(savedUser.getEmail());
        return response;
    }

    @Override
    public loginResponse login(loginRequest loginRequest) {
        var user = userRepository.findByUserID(loginRequest.getUserID()).orElseThrow(() -> new AppException("User Not found"));
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserID(), loginRequest.getPassword()));
        } catch (Exception e) {
            throw new AppException("Wrong Password");
        }
        var token = jwtUtils.generateToken(user);

        loginResponse response = new loginResponse();
        response.setToken(token);
        response.setUserID(user.getUserID());
        response.setRole(user.getRole().getRoleID());
        response.setAddress(user.getAddress());
        response.setName(user.getName());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setEmail(user.getEmail());
        return response;
    }

    @Override
    public userResponse getUserByID(String id) {
        User user = userRepository.findByUserID(id).orElseThrow(() -> new AppException("User Not Found"));
        userResponse response = new userResponse();
        response.setUserID(user.getUserID());
        response.setRole(user.getRole().getRoleID());
        response.setPassword(user.getPassword());
        response.setAddress(user.getAddress());
        response.setName(user.getName());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setEmail(user.getEmail());
        return response;
    }

    @Override
    public List<userResponse> getAllUser() {
        List<User> users = userRepository.findAll();
        List<userResponse> userResponses = new ArrayList<>();
        for (User user : users) {
            userResponse response = new userResponse();
            response.setUserID(user.getUserID());
            response.setRole(user.getRole().getRoleID());
            response.setPassword(user.getPassword());
            response.setAddress(user.getAddress());
            response.setName(user.getName());
            response.setPhoneNumber(user.getPhoneNumber());
            response.setEmail(user.getEmail());
            userResponses.add(response);
        }
        return userResponses;
    }

    @Override
    public List<userResponse> getAllUserByName(String name) {
        List<User> users = userRepository.findAllByNameContainingIgnoreCase(name);
        List<userResponse> userResponses = new ArrayList<>();
        for (User user : users) {
            userResponse response = new userResponse();
            response.setUserID(user.getUserID());
            response.setRole(user.getRole().getRoleID());
            response.setPassword(user.getPassword());
            response.setAddress(user.getAddress());
            response.setName(user.getName());
            response.setPhoneNumber(user.getPhoneNumber());
            response.setEmail(user.getEmail());
            userResponses.add(response);
        }
        return userResponses;
    }

    @Override
    public List<userResponse> getAllUserByRole(String role) {
        Role roleResponse = roleRepository.findByTitle(role).orElseThrow(() -> new AppException("Role not found"));
        List<User> users = userRepository.findAllByRole(roleResponse);
        List<userResponse> userResponses = new ArrayList<>();
        for (User user : users) {
            userResponse response = new userResponse();
            response.setUserID(user.getUserID());
            response.setRole(user.getRole().getRoleID());
            response.setPassword(user.getPassword());
            response.setAddress(user.getAddress());
            response.setName(user.getName());
            response.setPhoneNumber(user.getPhoneNumber());
            response.setEmail(user.getEmail());
            userResponses.add(response);
        }
        return userResponses;
    }

    @Override
    @Transactional
    public void deleteUserByID(String id) {
        User user = userRepository.findByUserID(id).orElseThrow(()-> new AppException("User not found"));
        if(user.getRole().getTitle().equals("VETERINARIAN")){
            Veterinarian veterinarian =veterinarianRepository.findByUser(user).orElseThrow(()-> new AppException("Veterinarian not found"));
            veterinarian.setStatus(false);
            return;
        }
        userRepository.deleteByUserID(id);
    }

    @Override
    public userResponse updateUser(userRequest userRequest) {
        userResponse response = new userResponse();
        User user = userRepository.findByUserID(userRequest.getUserID()).orElseThrow(() -> new AppException("User not found"));
        user.setUserID(userRequest.getUserID());
        Role role = roleRepository.findByTitle("CUSTOMER").orElseThrow(() -> new AppException("Role not found"));
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setAddress(userRequest.getAddress());
        user.setName(userRequest.getName());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setEmail(userRequest.getEmail());
        User savedUser = userRepository.save(user);
        response.setUserID(savedUser.getUserID());
        response.setRole(savedUser.getRole().getRoleID());
        response.setPassword(savedUser.getPassword());
        response.setAddress(savedUser.getAddress());
        response.setName(savedUser.getName());
        response.setPhoneNumber(savedUser.getPhoneNumber());
        response.setEmail(savedUser.getEmail());
        return response;
    }

    @Override
    public userResponse getLoginUser(String token) {
        String userID = token.substring(7);
        User user = userRepository.findByUserID(jwtUtils.extractUsername(userID)).orElseThrow(() -> new AppException("User Not Found"));
        userResponse response = new userResponse();
        response.setUserID(user.getUserID());
        response.setRole(user.getRole().getRoleID());
        response.setPassword(user.getPassword());
        response.setAddress(user.getAddress());
        response.setName(user.getName());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setEmail(user.getEmail());
        return response;
    }
}
