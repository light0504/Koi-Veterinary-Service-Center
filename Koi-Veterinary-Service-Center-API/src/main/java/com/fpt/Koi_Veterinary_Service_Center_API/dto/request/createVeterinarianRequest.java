package com.fpt.Koi_Veterinary_Service_Center_API.dto.request;

import com.fpt.Koi_Veterinary_Service_Center_API.entity.enums.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class createVeterinarianRequest {
    @NotBlank(message = "Description cannot be empty")
    private String Description;
    @NotBlank(message = "UserID cannot be empty")
    private String UserID;
}
