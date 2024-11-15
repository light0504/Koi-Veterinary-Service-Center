package com.fpt.Koi_Veterinary_Service_Center_API.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class slotResponse {
    private int slot;
    private LocalTime startTime;
    private LocalTime endTime;
}
