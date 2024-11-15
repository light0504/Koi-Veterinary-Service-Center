package com.fpt.Koi_Veterinary_Service_Center_API.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Veterinarians")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Veterinarian {
    @Id
    @GenericGenerator( name = "vetID", type = IdGenerator.class, parameters = {
            @org.hibernate.annotations.Parameter( name = IdGenerator.VALUE_PREFIX_PARAMETER, value = "V" ),
            @org.hibernate.annotations.Parameter( name = IdGenerator.NUMBER_FORMAT_PARAMETER, value = "%03d" ) } )
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "vetID" )
    private String veterinarianID;
    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String description;
    @Column(nullable = false)
    private Boolean status;
    @OneToOne
    @JoinColumn(name="userID", nullable=false)
    private User user;
}
