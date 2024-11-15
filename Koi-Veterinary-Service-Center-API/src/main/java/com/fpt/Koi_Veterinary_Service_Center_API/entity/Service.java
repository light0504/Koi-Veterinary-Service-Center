package com.fpt.Koi_Veterinary_Service_Center_API.entity;

import com.fpt.Koi_Veterinary_Service_Center_API.entity.enums.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;

@Entity
@Table(name = "Services")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Service {
    @Id
    @GenericGenerator( name = "seID", type = IdGenerator.class, parameters = {
            @org.hibernate.annotations.Parameter( name = IdGenerator.VALUE_PREFIX_PARAMETER, value = "S" ),
            @org.hibernate.annotations.Parameter( name = IdGenerator.NUMBER_FORMAT_PARAMETER, value = "%03d" ) } )
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "seID" )
    private String serviceID;
    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String name;
    @Column(columnDefinition = "NVARCHAR(255)")
    private String type;
    private Float price;
    private boolean isService;
    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails;
    private int maxQuantity;
}
