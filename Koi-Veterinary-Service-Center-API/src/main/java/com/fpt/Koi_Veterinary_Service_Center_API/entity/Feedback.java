package com.fpt.Koi_Veterinary_Service_Center_API.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "Feedbacks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Feedback {
    @Id
    @GenericGenerator( name = "feID", type = IdGenerator.class, parameters = {
            @org.hibernate.annotations.Parameter( name = IdGenerator.VALUE_PREFIX_PARAMETER, value = "FE" ),
            @org.hibernate.annotations.Parameter( name = IdGenerator.NUMBER_FORMAT_PARAMETER, value = "%01d" ) } )
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "feID" )
    private String feedbackId;
    private String comment;
    private Float rating;
    private LocalDateTime feedbackDateTime;
    @OneToOne
    @JoinColumn(name = "orderID", nullable = false)
    private Order order;
}
