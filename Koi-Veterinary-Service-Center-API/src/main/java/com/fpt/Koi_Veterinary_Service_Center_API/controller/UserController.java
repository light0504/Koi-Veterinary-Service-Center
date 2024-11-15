package com.fpt.Koi_Veterinary_Service_Center_API.controller;

import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.loginRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.userRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.loginResponse;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.userResponse;
import com.fpt.Koi_Veterinary_Service_Center_API.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody userRequest userRequest) {
        userResponse response = userService.register(userRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody loginRequest loginRequest) {
        loginResponse response = userService.login(loginRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user/Id/{id}")
//    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<?> getUserById(@PathVariable("id") String id) {
        userResponse user = userService.getUserByID(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/user/loginUser")
    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<?> getLoginUser(@RequestHeader("Authorization") String token) {
        userResponse user = userService.getLoginUser(token);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<?> getAllUser() {
        List<userResponse> users = userService.getAllUser();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/user/name/{name}")
    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<?> getAllUserByName(@PathVariable("name") String name) {
        List<userResponse> users = userService.getAllUserByName(name);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/user/role/{role}")
    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<?> getAllUserByRole(@PathVariable("role") String role) {
        List<userResponse> users = userService.getAllUserByRole(role);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @DeleteMapping("/user/delete/{id}")
    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") String id) {
        userService.deleteUserByID(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }

    @PutMapping("/user/update")
    @PreAuthorize("hasAuthority('Manager')")
    public ResponseEntity<?> updateUser(@Valid @RequestBody userRequest userRequest) {
        userResponse response = userService.updateUser(userRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
