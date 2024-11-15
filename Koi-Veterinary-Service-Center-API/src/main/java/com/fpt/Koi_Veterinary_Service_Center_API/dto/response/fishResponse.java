package com.fpt.Koi_Veterinary_Service_Center_API.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.User;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class fishResponse {
    private String fishID;
    private Float weight;
    private Float length;
    private int month;
    private String describe;
    private String userID;
}
