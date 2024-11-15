package com.fpt.Koi_Veterinary_Service_Center_API.service.impl;

import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.feedbackRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.feedbackResponse;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.Feedback;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.Invoice;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.Order;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.Veterinarian;
import com.fpt.Koi_Veterinary_Service_Center_API.exception.AppException;
import com.fpt.Koi_Veterinary_Service_Center_API.repository.FeedbackRepository;
import com.fpt.Koi_Veterinary_Service_Center_API.repository.InvoiceRepository;
import com.fpt.Koi_Veterinary_Service_Center_API.repository.OrderRepository;
import com.fpt.Koi_Veterinary_Service_Center_API.repository.VeterinarianRepository;
import com.fpt.Koi_Veterinary_Service_Center_API.service.IFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackServiceImpl implements IFeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private VeterinarianRepository veterinarianRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public feedbackResponse createFeedback(feedbackRequest feedbackRequest, String orderId) {
        Order order = orderRepository.findByOrderID(orderId).orElseThrow(()-> new AppException("Order not found"));
        Feedback feedback = new Feedback();
        feedback.setComment(feedbackRequest.getComment());
        feedback.setFeedbackDateTime(LocalDateTime.now());
        feedback.setRating(feedbackRequest.getRating());
        feedback.setOrder(order);
        Feedback savedFeedback = feedbackRepository.save(feedback);

        feedbackResponse response = new feedbackResponse();
        response.setFeedbackId(savedFeedback.getFeedbackId());
        response.setComment(savedFeedback.getComment());
        response.setRating(savedFeedback.getRating());
        response.setFeedbackDateTime(savedFeedback.getFeedbackDateTime());
        response.setOrderId(savedFeedback.getOrder().getOrderID());
        return response;
    }

    @Override
    public List<feedbackResponse> getAllFeedback() {
        List<feedbackResponse> responses = new ArrayList<>();
        List<Feedback> feedbacks = feedbackRepository.findAll();
        for (Feedback feedback : feedbacks){
            feedbackResponse response = new feedbackResponse();
            response.setFeedbackId(feedback.getFeedbackId());
            response.setComment(feedback.getComment());
            response.setRating(feedback.getRating());
            response.setFeedbackDateTime(feedback.getFeedbackDateTime());
            response.setOrderId(feedback.getOrder().getOrderID());
            responses.add(response);
        }
        return responses;
    }

    @Override
    public List<feedbackResponse> getVeterinaFeedback(String veterinaId) {
        Veterinarian veterinarian = veterinarianRepository.findByVeterinarianID(veterinaId).orElseThrow(()-> new AppException("Veterina not found"));
        List<Order> orders = orderRepository.findByVeterinarian(veterinarian);
        List<Feedback> feedbacks = new ArrayList<>();
        for (Order order : orders) {
            Feedback feedback = feedbackRepository.findByOrder(order).orElseThrow(()-> new AppException("order not found"));
            feedbacks.add(feedback);
        }

        List<feedbackResponse> responses = new ArrayList<>();
        for (Feedback feedback : feedbacks){
            feedbackResponse response = new feedbackResponse();
            response.setFeedbackId(feedback.getFeedbackId());
            response.setComment(feedback.getComment());
            response.setRating(feedback.getRating());
            response.setFeedbackDateTime(feedback.getFeedbackDateTime());
            response.setOrderId(feedback.getOrder().getOrderID());
            responses.add(response);
        }
        return responses;
    }
}
