package com.fpt.Koi_Veterinary_Service_Center_API.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class loginResponse {
    private String token;
    private String userID;
    private String name;
    private String email;
    private String phoneNumber;
    private String role;
    private String address;
}
