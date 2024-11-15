package com.fpt.Koi_Veterinary_Service_Center_API.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class serviceResponse {
    private String ServiceID;
    private String Name;
    private String Type;
    private Float price;
    private int maxQuantity;
    private boolean isService;
}
