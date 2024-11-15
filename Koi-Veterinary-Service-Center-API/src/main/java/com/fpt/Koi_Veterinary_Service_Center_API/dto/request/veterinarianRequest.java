package com.fpt.Koi_Veterinary_Service_Center_API.dto.request;

import com.fpt.Koi_Veterinary_Service_Center_API.entity.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class veterinarianRequest {
    @NotBlank(message = "VeterinaID cannot be empty")
    private String VeterinaID;
    @NotBlank(message = "Description cannot be empty")
    private String Description;
}
