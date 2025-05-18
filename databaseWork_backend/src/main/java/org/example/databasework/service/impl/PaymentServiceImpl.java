package org.example.databasework.service.impl;

import org.example.databasework.mapper.PatientMapper;
import org.example.databasework.mapper.PaymentMapper;
import org.example.databasework.model.Payment;
import org.example.databasework.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentMapper paymentMapper;
    @Autowired
    private PatientMapper patientMapper;

    @Override
    public Payment createPayment(Integer patientId, BigDecimal amount, String paymentType) {
        Payment payment = new Payment();
        payment.setPatient(patientMapper.findById(patientId));
        payment.setAmount(amount);
        payment.setPaymentType(paymentType);
        payment.setCreatedDate(java.time.LocalDateTime.now());
        paymentMapper.create(payment);
        return payment;
    }

    @Override
    public List<Payment> getPatientUnpayRecords(Integer patientId) {
        return paymentMapper.findUnmappedPaymentsByPatientId(patientId);
    }

    @Override
    public boolean completePayment(Integer paymentId, Integer patientId) {
        if (paymentMapper.findById(paymentId) == null || paymentMapper.findById(paymentId).getPatient().getPatientID() != patientId||paymentMapper.findById(paymentId).getPaidDate() != null) {
            return false;
        }else{
            paymentMapper.completePayment(paymentId, java.time.LocalDateTime.now());
            return true;
        }

    }

    @Override
    public List<Payment> getPatientPaymentHistory(Integer patientId) {
        return paymentMapper.findByPatientId(patientId);
    }

    @Override
    public String getPaymentStatus(Integer paymentId, Integer patientId) {
        Payment payment = paymentMapper.findById(paymentId);
        if (payment == null || payment.getPatient().getPatientID() != patientId) {
            return null;
        }
        if(payment.getPaidDate()==null){
            return "未支付";
        }else{
            return "已支付";
        }
    }
}
