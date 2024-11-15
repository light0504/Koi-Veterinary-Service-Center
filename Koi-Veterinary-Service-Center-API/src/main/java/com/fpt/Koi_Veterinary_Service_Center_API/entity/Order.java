package com.fpt.Koi_Veterinary_Service_Center_API.entity;

import com.fpt.Koi_Veterinary_Service_Center_API.entity.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GenericGenerator( name = "orderID", type = IdGenerator.class, parameters = {
            @org.hibernate.annotations.Parameter( name = IdGenerator.VALUE_PREFIX_PARAMETER, value = "O" ),
            @org.hibernate.annotations.Parameter( name = IdGenerator.NUMBER_FORMAT_PARAMETER, value = "%01d" ) } )
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "orderID" )
    private String orderID;
    private LocalDate orderDate;
    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String address;
    @Column(columnDefinition = "NVARCHAR(255)")
    private String description;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @ManyToOne
    @JoinColumn(name = "slot", nullable = false)
    private Slot slot;
    @ManyToOne
    @JoinColumn(name = "expenseID", nullable = false)
    private TravelExpense travelExpense;
    @ManyToOne
    @JoinColumn(name = "veterinarianID")
    private Veterinarian veterinarian;
    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private User user;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails;
}
