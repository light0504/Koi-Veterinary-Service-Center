package com.fpt.Koi_Veterinary_Service_Center_API.service.impl;

import com.fpt.Koi_Veterinary_Service_Center_API.dto.response.reportResponse;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.Invoice;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.Order;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.OrderDetail;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.User;
import com.fpt.Koi_Veterinary_Service_Center_API.repository.InvoiceRepository;
import com.fpt.Koi_Veterinary_Service_Center_API.repository.OrderDetailRepository;
import com.fpt.Koi_Veterinary_Service_Center_API.repository.OrderRepository;
import com.fpt.Koi_Veterinary_Service_Center_API.repository.UserRepository;
import com.fpt.Koi_Veterinary_Service_Center_API.service.IReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl implements IReportService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public reportResponse getAllReport() {
        List<Order> orders = orderRepository.findAll();
        List<Invoice> invoices = invoiceRepository.findAll();
        List<User> users = userRepository.findAll();

        //get customer from user
        List<User> customers = new ArrayList<>();
        for (User user: users) {
            if (user.getRole().getTitle().equals("Customer") ) {
                customers.add(user);
            }
        }

        //get all invoices total
        long total = 0;
        for(Invoice invoice: invoices){
            total += invoice.getTotal();
        }

        //find most used service
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();
        List<String> services = new ArrayList<>();
        for (OrderDetail orderDetail: orderDetails) {
            services.add(orderDetail.getService().getServiceID());
        }
        String bestService = mostFrequent(services, services.size());

        //create response
        reportResponse reportResponse = new reportResponse();
        reportResponse.setTotalOrders(orders.size());
        reportResponse.setTotalCustomers(customers.size());
        reportResponse.setTotalPayments(total);
        reportResponse.setBestService(bestService);
        return reportResponse;
    }

    private String mostFrequent(List<String> arr, int n)
    {
        int maxcount = 0;
        String element_having_max_freq = null;
        for (int i = 0; i < n; i++) {
            int count = 0;
            for (int j = 0; j < n; j++) {
                if (arr.get(i) == arr.get(j)) {
                    count++;
                }
            }

            if (count > maxcount) {
                maxcount = count;
                element_having_max_freq = arr.get(i);
            }
        }

        return element_having_max_freq;
    }
}
