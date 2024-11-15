package com.fpt.Koi_Veterinary_Service_Center_API.service;

import com.fpt.Koi_Veterinary_Service_Center_API.dto.request.feedbackRequest;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.feedbackResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface IFeedbackService {

    feedbackResponse createFeedback(@Valid feedbackRequest feedbackRequest, String orderId);

    List<feedbackResponse> getAllFeedback();

    List<feedbackResponse> getVeterinaFeedback(String veterinaId);
}
