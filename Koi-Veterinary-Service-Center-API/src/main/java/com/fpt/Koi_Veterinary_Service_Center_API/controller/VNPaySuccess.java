package com.fpt.Koi_Veterinary_Service_Center_API.controller;

import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.invoiceResponse;
import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.paymentResponse;
import com.fpt.Koi_Veterinary_Service_Center_API.service.IVNPAYService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api")
public class VNPaySuccess {
    @Autowired
    private IVNPAYService ivnpayService;

    @GetMapping("/payment-success")
    public String paymentSuccess(@RequestParam("vnp_OrderInfo") String orderInfo, @RequestParam("vnp_Amount") String Total, @RequestParam("vnp_ResponseCode") String responseCode) {
        paymentResponse response = ivnpayService.paymentSuccess(responseCode, Total, orderInfo);
        String url = null;
        if(response.getInvoiceId()==null){
            url = response.getUrl();
        }
        else {
            url = String.format(response.getUrl() + "?invoiceId=%s&total=%s&invDate=%s&orderId=%s",
                    response.getInvoiceId(),response.getTotal(),response.getInvDate(),response.getOrderId());
        }
        return "redirect:" + url;
    }
}
