package com.fpt.Koi_Veterinary_Service_Center_API.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Fish")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@IdClass(FishID.class)
public class Fish {
    @Id
    @GenericGenerator( name = "fishID", type = IdGenerator.class, parameters = {
            @org.hibernate.annotations.Parameter( name = IdGenerator.VALUE_PREFIX_PARAMETER, value = "F" ),
            @org.hibernate.annotations.Parameter( name = IdGenerator.NUMBER_FORMAT_PARAMETER, value = "%01d" ) } )
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "fishID" )
    private String fishID;
    private Float weight;
    private Float length;
    private int month;
    private String describe;
    @Id
    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User user;
}
