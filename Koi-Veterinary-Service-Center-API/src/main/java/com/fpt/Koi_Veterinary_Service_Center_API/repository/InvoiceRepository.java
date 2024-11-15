package com.fpt.Koi_Veterinary_Service_Center_API.repository;

import com.fpt.Koi_Veterinary_Service_Center_API.entity.Invoice;
import com.fpt.Koi_Veterinary_Service_Center_API.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, String > {
    Optional<Invoice> findByInvoiceID(String invoiceId);
    Optional<Invoice> findByOrder(Order order);
}
