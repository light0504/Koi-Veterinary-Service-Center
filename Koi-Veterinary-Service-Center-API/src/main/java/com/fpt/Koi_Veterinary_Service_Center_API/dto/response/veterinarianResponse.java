package com.fpt.Koi_Veterinary_Service_Center_API.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.enums.Status;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class veterinarianResponse {
    private String VeterinaID;
    private String Description;
    private Boolean Status;
    private String UserID;
    private String name;
}
