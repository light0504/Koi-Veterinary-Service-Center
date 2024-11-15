package com.fpt.Koi_Veterinary_Service_Center_API.controller;

import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.createOrderRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.orderDescriptionRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.orderServiceReqest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.orderStatusRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.orderResponse;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.OrderDetail;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.Slot;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.enums.OrderStatus;
import com.fpt.Koi_Veterinary_Service_Center_API.service.IOrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {
    @Autowired
    private IOrderService orderService;
    @PostMapping("/orders")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('Staff') or hasAuthority('Customer')")
    public ResponseEntity<?> createOrder(@Valid @RequestBody createOrderRequest createOrderRequest) {
        orderResponse response = orderService.addOrder(createOrderRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/orders")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('Staff')")
    public ResponseEntity<?> getAllOrder() {
        List<orderResponse> responses = orderService.getAllOrder();
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/orders/{orderId}")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('Staff') or hasAuthority('Veterina') or hasAuthority('Customer')")
    public ResponseEntity<?> getOrderByOrderID(@PathVariable("orderId") String orderId) {
        orderResponse response = orderService.getOrderByOrderID(orderId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/orders/OrderAndSlot/{orderDate}/{slot}")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('Staff') or hasAuthority('Customer')")
    public ResponseEntity<?> getOrderByOrderDateAndSlot(@PathVariable("orderDate") LocalDate orderDate, @PathVariable("slot") int slot) {
        List<orderResponse> responses = orderService.getOrderByOrderDateAndSlot(orderDate, slot);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/orders/veterina/{veterinaId}")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('Staff') or hasAuthority('Veterina') or hasAuthority('Customer')")
    public ResponseEntity<?> getOrderByVeterinaID(@PathVariable("veterinaId") String veterinaId) {
        List<orderResponse> responses = orderService.getOrderByVeterinaID(veterinaId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @GetMapping("/orders/user/{userId}")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('Staff') or hasAuthority('Customer')")
    public ResponseEntity<?> getOrderByUserId(@PathVariable("userId") String UserId) {
        List<orderResponse> responses = orderService.getOrderByUserId(UserId);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PutMapping("/orders/{orderId}/veterina/{veterinaId}")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('Staff')")
    public ResponseEntity<?> updateVeterinaInOrder(@PathVariable("orderId") String orderId,@PathVariable("veterinaId") String veterinaId) {
        orderResponse response = orderService.updateVeterinaInOrder(orderId,veterinaId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/orders/{orderId}/status")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('Staff') or hasAuthority('Veterina') or hasAuthority('Customer')")
    public ResponseEntity<?> updateOrderStatus(@PathVariable("orderId") String orderId, @Valid @RequestBody orderStatusRequest status) {
        orderResponse response = orderService.updateOrderStatus(orderId, status);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/orders/{orderId}/description")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('Staff') or hasAuthority('Veterina')")
    public ResponseEntity<?> addOrderDescription(@PathVariable("orderId") String orderId, @Valid @RequestBody orderDescriptionRequest description) {
        orderResponse response = orderService.addOrderDescription(orderId, description);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/orders/{orderId}/services")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('Staff') or hasAuthority('Veterina')")
    public ResponseEntity<?> addServiceToOrder(@PathVariable("orderId") String orderId, @Valid @RequestBody orderServiceReqest orderServiceReq) {
        orderResponse response = orderService.addServiceToOrder(orderId, orderServiceReq);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/orders/{orderId}/services/{serviceID}")
    @PreAuthorize("hasAuthority('Manager') or hasAuthority('Staff') or hasAuthority('Veterina')")
    public ResponseEntity<?> removeServiceFromOrder(@PathVariable("orderId") String orderId, @PathVariable("serviceID") String serviceID) {
        orderResponse response = orderService.removeServiceFromOrder(orderId,serviceID);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
