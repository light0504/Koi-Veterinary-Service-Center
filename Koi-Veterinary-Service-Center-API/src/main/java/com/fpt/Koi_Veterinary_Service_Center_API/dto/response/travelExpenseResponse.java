package com.fpt.Koi_Veterinary_Service_Center_API.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class travelExpenseResponse {
    private String ExpenseID;
    private int Fee;
    private String EndLocation;
}
