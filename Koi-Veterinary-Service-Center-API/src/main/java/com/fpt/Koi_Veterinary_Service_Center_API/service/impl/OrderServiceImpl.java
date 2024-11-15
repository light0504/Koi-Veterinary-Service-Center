package com.fpt.Koi_Veterinary_Service_Center_API.service.impl;

import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.*;

import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.OrderDetailResponse;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.invoiceResponse;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.orderResponse;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.*;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.enums.OrderStatus;
import com.fpt.Koi_Veterinary_Service_Center_API.exception.AppException;
import com.fpt.Koi_Veterinary_Service_Center_API.repository.*;
import com.fpt.Koi_Veterinary_Service_Center_API.service.IOrderService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VeterinarianRepository veterinarianRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private SlotRepository slotRepository;
    @Autowired
    private TravelExpenseRepository travelExpenseRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;

    @Override
    public orderResponse addOrder(createOrderRequest createOrderRequest) {

        User user = userRepository.findByUserID(createOrderRequest.getUserID()).orElseThrow(()-> new AppException("User not found"));
        Slot slot = slotRepository.findBySlot(createOrderRequest.getSlot()).orElseThrow(()-> new AppException("Slot not found"));

        //check if address in request is valid
        List<TravelExpense> travelExpenses = travelExpenseRepository.findAll();
        TravelExpense travelExpense = null;
        for(TravelExpense singleTravelExpense : travelExpenses){
            if(createOrderRequest.getAddress().contains(singleTravelExpense.getEndLocation())){
                travelExpense = singleTravelExpense;
                break;
            }
        }
        if(travelExpense == null){
            throw new AppException("Location not registered");
        }

        //create new order
        Order order = new Order();
        if(createOrderRequest.getVeterinaID()!=null){
            Veterinarian veterinarian = veterinarianRepository.findByVeterinarianID(createOrderRequest.getVeterinaID()).orElseThrow(()-> new AppException("Veterinarian not found"));
            List<Order> orders = orderRepository.findByVeterinarianAndOrderDateAndSlot(veterinarian,createOrderRequest.getDate(), slot);
            if (orders == null || orders.isEmpty()) {
                order.setVeterinarian(veterinarian);
            }
            else throw new AppException("Veterinarian not available");
        }
        order.setUser(user);
        order.setOrderDate(createOrderRequest.getDate());
        order.setSlot(slot);
        order.setAddress(createOrderRequest.getAddress());
        order.setTravelExpense(travelExpense);
        order.setStatus(OrderStatus.pending);
        order.setOrderDetails(new ArrayList<>());
        Order newOrder = orderRepository.save(order);

        //create order details
        List<OrderDetail> orderDetails = newOrder.getOrderDetails();
        List<orderDetailRequest> orderDetailRequests = createOrderRequest.getServices();
        for (orderDetailRequest orderDetailRequest : orderDetailRequests){
            com.fpt.Koi_Veterinary_Service_Center_API.entity.Service service = serviceRepository.findByServiceID(orderDetailRequest.getServiceID()).orElseThrow(()-> new AppException("Service not found"));
            isServiceExist(newOrder, service);
            OrderDetail newOrderDetail = new OrderDetail();
            newOrderDetail.setOrder(newOrder);
            newOrderDetail.setQuantity(orderDetailRequest.getQuantity());
            newOrderDetail.setService(service);
            orderDetails.add(newOrderDetail);
        }
        newOrder.setOrderDetails(orderDetails);
        Order savedOrder = orderRepository.save(newOrder);

        //create response
        orderResponse response = new orderResponse();
        List<OrderDetailResponse> detailResponses = new ArrayList<>();
        for (OrderDetail orderDetail1 : savedOrder.getOrderDetails()){
            OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
            orderDetailResponse.setQuantity(orderDetail1.getQuantity());
            orderDetailResponse.setServiceID(orderDetail1.getService().getServiceID());
            detailResponses.add(orderDetailResponse);
        }
        response.setAddress(savedOrder.getAddress());
        response.setSlot(savedOrder.getSlot().getSlot());
        response.setOrderId(savedOrder.getOrderID());
        response.setStatus(savedOrder.getStatus());
        response.setOrderDate(savedOrder.getOrderDate());
        if(savedOrder.getVeterinarian()!=null){
            response.setVeterinaId(savedOrder.getVeterinarian().getVeterinarianID());
        }
        response.setTravelExpenseId(savedOrder.getTravelExpense().getExpenseID());
        response.setDescription(savedOrder.getDescription());
        response.setServices(detailResponses);
        response.setUserId(savedOrder.getUser().getUserID());
        return response;
    }

    @Override
    public List<orderResponse> getAllOrder() {
        List<Order> orders = orderRepository.findAll();
        List<orderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders) {
            orderResponse response = new orderResponse();
            List<OrderDetailResponse> detailResponses = new ArrayList<>();
            for (OrderDetail orderDetail1 : order.getOrderDetails()){
                OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
                orderDetailResponse.setQuantity(orderDetail1.getQuantity());
                orderDetailResponse.setServiceID(orderDetail1.getService().getServiceID());
                detailResponses.add(orderDetailResponse);
            }
            response.setAddress(order.getAddress());
            response.setSlot(order.getSlot().getSlot());
            response.setOrderId(order.getOrderID());
            response.setStatus(order.getStatus());
            response.setOrderDate(order.getOrderDate());
            if(order.getVeterinarian()!=null){
                response.setVeterinaId(order.getVeterinarian().getVeterinarianID());
            }
            response.setTravelExpenseId(order.getTravelExpense().getExpenseID());
            response.setDescription(order.getDescription());
            response.setServices(detailResponses);
            response.setUserId(order.getUser().getUserID());
            orderResponses.add(response);
        }
        return orderResponses;
    }

    @Override
    public orderResponse getOrderByOrderID(String orderId) {
        Order order = orderRepository.findByOrderID(orderId).orElseThrow(()-> new AppException("Order not found"));
        orderResponse response = new orderResponse();
        List<OrderDetailResponse> detailResponses = new ArrayList<>();
        for (OrderDetail orderDetail1 : order.getOrderDetails()){
            OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
            orderDetailResponse.setQuantity(orderDetail1.getQuantity());
            orderDetailResponse.setServiceID(orderDetail1.getService().getServiceID());
            detailResponses.add(orderDetailResponse);
        }
        response.setAddress(order.getAddress());
        response.setSlot(order.getSlot().getSlot());
        response.setOrderId(order.getOrderID());
        response.setStatus(order.getStatus());
        response.setOrderDate(order.getOrderDate());
        if(order.getVeterinarian()!=null){
            response.setVeterinaId(order.getVeterinarian().getVeterinarianID());
        }
        response.setTravelExpenseId(order.getTravelExpense().getExpenseID());
        response.setDescription(order.getDescription());
        response.setServices(detailResponses);
        response.setUserId(order.getUser().getUserID());
        return response;
    }

    @Override
    public orderResponse updateOrderStatus(String orderId, orderStatusRequest status) {
        Order order = orderRepository.findByOrderID(orderId).orElseThrow(()-> new AppException("Order not found"));
        order.setStatus(status.getStatus());
        Order savedOrder = orderRepository.save(order);
        if(status.getStatus()== OrderStatus.done){
            int total = order.getTravelExpense().getFee();
            List<OrderDetail> orderDetails = order.getOrderDetails();
            for(OrderDetail orderDetail : orderDetails){
                total += orderDetail.getService().getPrice() * orderDetail.getQuantity();
            }
            Invoice invoice = new Invoice();
            invoice.setInvDate(LocalDateTime.now());
            invoice.setTotal(total);
            invoice.setOrder(order);
            invoice.setMethod("cash");
            Invoice savedInvoice = invoiceRepository.save(invoice);

            orderResponse response = new orderResponse();
            response.setInvDate(savedInvoice.getInvDate());
            response.setOrderId(savedInvoice.getOrder().getOrderID());
            response.setTotal(savedInvoice.getTotal());
            response.setInvoiceId(savedInvoice.getInvoiceID());
            response.setMethod(savedInvoice.getMethod());
            return response;
        }

        orderResponse response = new orderResponse();
        List<OrderDetailResponse> detailResponses = new ArrayList<>();
        for (OrderDetail orderDetail1 : savedOrder.getOrderDetails()){
            OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
            orderDetailResponse.setQuantity(orderDetail1.getQuantity());
            orderDetailResponse.setServiceID(orderDetail1.getService().getServiceID());
            detailResponses.add(orderDetailResponse);
        }
        response.setAddress(savedOrder.getAddress());
        response.setSlot(savedOrder.getSlot().getSlot());
        response.setOrderId(savedOrder.getOrderID());
        response.setStatus(savedOrder.getStatus());
        response.setOrderDate(savedOrder.getOrderDate());
        if(savedOrder.getVeterinarian()!=null){
            response.setVeterinaId(savedOrder.getVeterinarian().getVeterinarianID());
        }
        response.setTravelExpenseId(savedOrder.getTravelExpense().getExpenseID());
        response.setDescription(savedOrder.getDescription());
        response.setServices(detailResponses);
        response.setUserId(savedOrder.getUser().getUserID());
        return response;
    }

    @Override
    public orderResponse addOrderDescription(String orderId, orderDescriptionRequest description) {
        Order order = orderRepository.findByOrderID(orderId).orElseThrow(()-> new AppException("Order not found"));
        order.setDescription(description.getDescription());
        Order savedOrder = orderRepository.save(order);

        orderResponse response = new orderResponse();
        List<OrderDetailResponse> detailResponses = new ArrayList<>();
        for (OrderDetail orderDetail1 : savedOrder.getOrderDetails()){
            OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
            orderDetailResponse.setQuantity(orderDetail1.getQuantity());
            orderDetailResponse.setServiceID(orderDetail1.getService().getServiceID());
            detailResponses.add(orderDetailResponse);
        }
        response.setAddress(savedOrder.getAddress());
        response.setSlot(savedOrder.getSlot().getSlot());
        response.setOrderId(savedOrder.getOrderID());
        response.setStatus(savedOrder.getStatus());
        response.setOrderDate(savedOrder.getOrderDate());
        if(savedOrder.getVeterinarian()!=null){
            response.setVeterinaId(savedOrder.getVeterinarian().getVeterinarianID());
        }
        response.setTravelExpenseId(savedOrder.getTravelExpense().getExpenseID());
        response.setDescription(savedOrder.getDescription());
        response.setServices(detailResponses);
        response.setUserId(savedOrder.getUser().getUserID());
        return response;
    }

    @Override
    public orderResponse addServiceToOrder(String orderId, orderServiceReqest orderServiceReq) {

        Order order = orderRepository.findByOrderID(orderId).orElseThrow(()-> new AppException("Order not found"));
        com.fpt.Koi_Veterinary_Service_Center_API.entity.Service service = serviceRepository.findByServiceID(orderServiceReq.getServiceID()).orElseThrow(()-> new AppException("Service not found"));
        isServiceExist(order, service);

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setService(service);
        orderDetail.setQuantity(orderServiceReq.getQuantity());
        orderDetail.setOrder(order);
        order.getOrderDetails().add(orderDetail);
        Order savedOrder = orderRepository.save(order);

        orderResponse response = new orderResponse();
        List<OrderDetailResponse> detailResponses = new ArrayList<>();
        for (OrderDetail orderDetail1 : savedOrder.getOrderDetails()){
            OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
            orderDetailResponse.setQuantity(orderDetail1.getQuantity());
            orderDetailResponse.setServiceID(orderDetail1.getService().getServiceID());
            detailResponses.add(orderDetailResponse);
        }
        response.setAddress(savedOrder.getAddress());
        response.setSlot(savedOrder.getSlot().getSlot());
        response.setOrderId(savedOrder.getOrderID());
        response.setStatus(savedOrder.getStatus());
        response.setOrderDate(savedOrder.getOrderDate());
        if(savedOrder.getVeterinarian()!=null){
            response.setVeterinaId(savedOrder.getVeterinarian().getVeterinarianID());
        }
        response.setTravelExpenseId(savedOrder.getTravelExpense().getExpenseID());
        response.setDescription(savedOrder.getDescription());
        response.setServices(detailResponses);
        response.setUserId(savedOrder.getUser().getUserID());
        return response;
    }

    private void isServiceExist(Order order, com.fpt.Koi_Veterinary_Service_Center_API.entity.Service service) {
        for (OrderDetail orderDetail : order.getOrderDetails()){
            if(orderDetail.getService().equals(service)){
                throw new AppException("Service already in order");
            }
        }
    }

    @Override
    public orderResponse removeServiceFromOrder(String orderId, String serviceID) {
        Order order = orderRepository.findByOrderID(orderId).orElseThrow(()-> new AppException("Order not found"));
        com.fpt.Koi_Veterinary_Service_Center_API.entity.Service service = serviceRepository.findByServiceID(serviceID).orElseThrow(()-> new AppException("Service not found"));

        OrderDetail orderDetail = orderDetailRepository.findByServiceAndOrder(service, order).orElseThrow(()-> new AppException("OrderDetail not found"));
        order.getOrderDetails().remove(orderDetail);
        Order savedOrder = orderRepository.save(order);

        orderResponse response = new orderResponse();
        List<OrderDetailResponse> detailResponses = new ArrayList<>();
        for (OrderDetail orderDetail1 : savedOrder.getOrderDetails()){
            OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
            orderDetailResponse.setQuantity(orderDetail1.getQuantity());
            orderDetailResponse.setServiceID(orderDetail1.getService().getServiceID());
            detailResponses.add(orderDetailResponse);
        }
        response.setAddress(savedOrder.getAddress());
        response.setSlot(savedOrder.getSlot().getSlot());
        response.setOrderId(savedOrder.getOrderID());
        response.setStatus(savedOrder.getStatus());
        response.setOrderDate(savedOrder.getOrderDate());
        if(savedOrder.getVeterinarian()!=null){
            response.setVeterinaId(savedOrder.getVeterinarian().getVeterinarianID());
        }
        response.setTravelExpenseId(savedOrder.getTravelExpense().getExpenseID());
        response.setDescription(savedOrder.getDescription());
        response.setServices(detailResponses);
        response.setUserId(savedOrder.getUser().getUserID());
        return response;
    }

    @Override
    public List<orderResponse> getOrderByOrderDateAndSlot(LocalDate orderDate, int slot) {
        Slot slot1 = slotRepository.findBySlot(slot).orElseThrow(()-> new AppException("Slot not found"));
        List<Order> orders = orderRepository.findByOrderDateAndSlot(orderDate, slot1);
        List<orderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders) {
            orderResponse response = new orderResponse();
            List<OrderDetailResponse> detailResponses = new ArrayList<>();
            for (OrderDetail orderDetail1 : order.getOrderDetails()){
                OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
                orderDetailResponse.setQuantity(orderDetail1.getQuantity());
                orderDetailResponse.setServiceID(orderDetail1.getService().getServiceID());
                detailResponses.add(orderDetailResponse);
            }
            response.setAddress(order.getAddress());
            response.setSlot(order.getSlot().getSlot());
            response.setOrderId(order.getOrderID());
            response.setStatus(order.getStatus());
            response.setOrderDate(order.getOrderDate());
            if(order.getVeterinarian()!=null){
                response.setVeterinaId(order.getVeterinarian().getVeterinarianID());
            }
            response.setTravelExpenseId(order.getTravelExpense().getExpenseID());
            response.setDescription(order.getDescription());
            response.setServices(detailResponses);
            response.setUserId(order.getUser().getUserID());
            orderResponses.add(response);
        }
        return orderResponses;
    }

    @Override
    public List<orderResponse> getOrderByVeterinaID(String veterinaId) {
        Veterinarian veterinarian = veterinarianRepository.findByVeterinarianID(veterinaId).orElseThrow(()-> new AppException("Veterina not found"));
        List<Order> orders = orderRepository.findByVeterinarian(veterinarian);
        List<orderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders) {
            orderResponse response = new orderResponse();
            List<OrderDetailResponse> detailResponses = new ArrayList<>();
            for (OrderDetail orderDetail1 : order.getOrderDetails()){
                OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
                orderDetailResponse.setQuantity(orderDetail1.getQuantity());
                orderDetailResponse.setServiceID(orderDetail1.getService().getServiceID());
                detailResponses.add(orderDetailResponse);
            }
            response.setAddress(order.getAddress());
            response.setSlot(order.getSlot().getSlot());
            response.setOrderId(order.getOrderID());
            response.setStatus(order.getStatus());
            response.setOrderDate(order.getOrderDate());
            if(order.getVeterinarian()!=null){
                response.setVeterinaId(order.getVeterinarian().getVeterinarianID());
            }
            response.setTravelExpenseId(order.getTravelExpense().getExpenseID());
            response.setDescription(order.getDescription());
            response.setServices(detailResponses);
            response.setUserId(order.getUser().getUserID());
            orderResponses.add(response);
        }
        return orderResponses;
    }

    @Override
    public List<orderResponse> getOrderByUserId(String userId) {
        User user = userRepository.findByUserID(userId).orElseThrow(()-> new AppException("User not found"));
        List<Order> orders = orderRepository.findByUser(user);
        List<orderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders) {
            orderResponse response = new orderResponse();
            List<OrderDetailResponse> detailResponses = new ArrayList<>();
            for (OrderDetail orderDetail1 : order.getOrderDetails()){
                OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
                orderDetailResponse.setQuantity(orderDetail1.getQuantity());
                orderDetailResponse.setServiceID(orderDetail1.getService().getServiceID());
                detailResponses.add(orderDetailResponse);
            }
            response.setAddress(order.getAddress());
            response.setSlot(order.getSlot().getSlot());
            response.setOrderId(order.getOrderID());
            response.setStatus(order.getStatus());
            response.setOrderDate(order.getOrderDate());
            if(order.getVeterinarian()!=null){
                response.setVeterinaId(order.getVeterinarian().getVeterinarianID());
            }
            response.setTravelExpenseId(order.getTravelExpense().getExpenseID());
            response.setDescription(order.getDescription());
            response.setServices(detailResponses);
            response.setUserId(order.getUser().getUserID());
            orderResponses.add(response);
        }
        return orderResponses;
    }

    @Override
    public orderResponse updateVeterinaInOrder(String orderId, String veterinaId) {
        Veterinarian veterinarian = veterinarianRepository.findByVeterinarianID(veterinaId).orElseThrow(()-> new AppException("Veterina not found"));
        Order order = orderRepository.findByOrderID(orderId).orElseThrow(()-> new AppException("Order not found"));
        order.setVeterinarian(veterinarian);
        Order savedOrder = orderRepository.save(order);

        orderResponse response = new orderResponse();
        List<OrderDetailResponse> detailResponses = new ArrayList<>();
        for (OrderDetail orderDetail1 : savedOrder.getOrderDetails()){
            OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
            orderDetailResponse.setQuantity(orderDetail1.getQuantity());
            orderDetailResponse.setServiceID(orderDetail1.getService().getServiceID());
            detailResponses.add(orderDetailResponse);
        }
        response.setAddress(savedOrder.getAddress());
        response.setSlot(savedOrder.getSlot().getSlot());
        response.setOrderId(savedOrder.getOrderID());
        response.setStatus(savedOrder.getStatus());
        response.setOrderDate(savedOrder.getOrderDate());
        if(savedOrder.getVeterinarian()!=null){
            response.setVeterinaId(savedOrder.getVeterinarian().getVeterinarianID());
        }
        response.setTravelExpenseId(savedOrder.getTravelExpense().getExpenseID());
        response.setDescription(savedOrder.getDescription());
        response.setServices(detailResponses);
        response.setUserId(savedOrder.getUser().getUserID());
        return response;
    }
}
