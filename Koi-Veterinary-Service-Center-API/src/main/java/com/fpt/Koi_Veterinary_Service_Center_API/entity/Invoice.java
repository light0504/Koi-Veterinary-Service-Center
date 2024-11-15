package com.fpt.Koi_Veterinary_Service_Center_API.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "Invoice")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
    @Id
    @GenericGenerator( name = "invoiceID", type = IdGenerator.class, parameters = {
            @org.hibernate.annotations.Parameter( name = IdGenerator.VALUE_PREFIX_PARAMETER, value = "I" ),
            @org.hibernate.annotations.Parameter( name = IdGenerator.NUMBER_FORMAT_PARAMETER, value = "%01d" ) } )
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "invoiceID" )
    private String invoiceID;
    private int total;
    private LocalDateTime invDate;
    private String method;
    @OneToOne
    @JoinColumn(name = "orderID", nullable = false)
    private Order order;
}
