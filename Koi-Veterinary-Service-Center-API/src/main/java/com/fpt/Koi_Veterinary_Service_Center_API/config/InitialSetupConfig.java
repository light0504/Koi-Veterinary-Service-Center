package com.fpt.Koi_Veterinary_Service_Center_API.config;

import com.fpt.Koi_Veterinary_Service_Center_API.entity.Fish;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.Role;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.User;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.Veterinarian;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.enums.Status;
import com.fpt.Koi_Veterinary_Service_Center_API.exception.AppException;
import com.fpt.Koi_Veterinary_Service_Center_API.repository.FishRepository;
import com.fpt.Koi_Veterinary_Service_Center_API.repository.RoleRepository;
import com.fpt.Koi_Veterinary_Service_Center_API.repository.UserRepository;
import com.fpt.Koi_Veterinary_Service_Center_API.repository.VeterinarianRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import static java.lang.Boolean.TRUE;

@Configuration
public class InitialSetupConfig {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final FishRepository fishRepository;
    private final VeterinarianRepository veterinarianRepository;

    public InitialSetupConfig(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, FishRepository fishRepository, VeterinarianRepository veterinarianRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.fishRepository = fishRepository;
        this.veterinarianRepository = veterinarianRepository;
    }

    @Bean
    public ApplicationRunner initializer() {

        return args -> {
            if(roleRepository.findByTitle("Manager").isEmpty()){
                Role admin = new Role();
                admin.setTitle("Manager");
                admin.setRoleID("M");
                roleRepository.save(admin);
            }
            if(roleRepository.findByTitle("Customer").isEmpty()){
                Role customer = new Role();
                customer.setTitle("Customer");
                customer.setRoleID("C");
                roleRepository.save(customer);
            }
            if(roleRepository.findByTitle("Veterina").isEmpty()){
                Role veterinarian = new Role();
                veterinarian.setTitle("Veterina");
                veterinarian.setRoleID("V");
                roleRepository.save(veterinarian);
            }
            if(roleRepository.findByTitle("Staff").isEmpty()){
                Role staff = new Role();
                staff.setTitle("Staff");
                staff.setRoleID("S");
                roleRepository.save(staff);
            }
            if (!userRepository.existsByUserID("admin")) {
                User user = new User();
                user.setUserID("admin");
                user.setEmail("admin@gmail.com");
                user.setName("admin");
                user.setPhoneNumber("0123456789");
                user.setPassword(passwordEncoder.encode("123"));
                user.setRole(roleRepository.findByTitle("Manager").orElseThrow(() -> new AppException("Role not found")));
                userRepository.save(user);
            }
            if (!userRepository.existsByUserID("customer")) {
                User user = new User();
                user.setUserID("customer");
                user.setEmail("customer@gmail.com");
                user.setName("customer");
                user.setPhoneNumber("0123456789");
                user.setPassword(passwordEncoder.encode("123"));
                user.setRole(roleRepository.findByTitle("Customer").orElseThrow(() -> new AppException("Role not found")));
                userRepository.save(user);
            }
            if (!userRepository.existsByUserID("staff")) {
                User user = new User();
                user.setUserID("staff");
                user.setEmail("staff@gmail.com");
                user.setName("staff");
                user.setPhoneNumber("0123456789");
                user.setPassword(passwordEncoder.encode("123"));
                user.setRole(roleRepository.findByTitle("Staff").orElseThrow(() -> new AppException("Role not found")));
                userRepository.save(user);
            }
        };
    }
}
