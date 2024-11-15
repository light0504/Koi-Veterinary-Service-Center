package com.fpt.Koi_Veterinary_Service_Center_API.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.Role;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class userResponse {
    private String userID;
    private String password;
    private String name;
    private String email;
    private String phoneNumber;
    private String role;
    private String address;
}
