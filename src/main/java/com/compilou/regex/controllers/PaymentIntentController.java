package com.compilou.regex.controllers;

import com.compilou.regex.mapper.request.StripeRequest;
import com.compilou.regex.models.User;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/create-payment-intent")
@Tag(name = "Stripe", description = "Endpoints for Managing Payment With Stripe")
public class PaymentIntentController {

    @Value("${stripe.api.secretKey}")
    private String stripeApiKey;

    @PostMapping
    @Operation(summary = "Create Payment Intent With Stripe", description = "Create Payment Intent With Stripe",
            tags = {"Stripe"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            array = @ArraySchema(schema = @Schema(implementation = User.class))
                                    )
                            }),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
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