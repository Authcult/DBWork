package org.example.databasework.service;

import org.example.databasework.model.Payment;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentService {
    Payment createPayment(Integer patientId, BigDecimal amount, String paymentType);

    List<Payment> getPatientUnpayRecords(Integer patientId);

    boolean completePayment(Integer paymentId, Integer patientId);

    List<Payment> getPatientPaymentHistory(Integer patientId);

    String getPaymentStatus(Integer paymentId, Integer patientId);
}
