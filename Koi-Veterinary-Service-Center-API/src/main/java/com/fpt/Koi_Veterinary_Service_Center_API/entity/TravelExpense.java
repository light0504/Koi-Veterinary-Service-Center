package com.fpt.Koi_Veterinary_Service_Center_API.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "TravelExpenses")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TravelExpense {
    @Id
    private String expenseID;
    @Column(nullable = false)
    private int Fee;
    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String endLocation;
}
