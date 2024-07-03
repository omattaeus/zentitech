package com.compilou.regex.controllers;

import com.compilou.regex.mapper.request.StripeRequest;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/create-payment-intent")
public class PaymentIntentController {

    @Value("${stripe.api.secretKey}")
    private String stripeApiKey;

    @PostMapping
    public Map<String, Object> createPaymentIntent(@RequestBody StripeRequest stripeRequest) throws Exception {
        Stripe.apiKey = stripeApiKey;

        Long amountInCents = stripeRequest.convertToLong(stripeRequest.getAmount());

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("https://dashboard.stripe.com/test/payments/{CHECKOUT_SESSION_ID}")
                .setCancelUrl("https://zentitech.tech/")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("BRL")
                                                .setUnitAmount(amountInCents)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(stripeRequest.getProductName())
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .setCustomerEmail(stripeRequest.getEmail())
                .build();

        Session session = Session.create(params);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("sessionId", session.getId());

        return responseData;
    }
}