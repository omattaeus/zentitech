package com.compilou.regex.services;

import com.compilou.regex.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Long getUserIdByPaymentIntent(String paymentIntentId) {
        return paymentRepository.findUserIdByPaymentIntentId(paymentIntentId);
    }
}
