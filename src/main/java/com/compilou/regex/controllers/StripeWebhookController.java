package com.compilou.regex.controllers;

import com.compilou.regex.models.User;
import com.compilou.regex.repositories.UserRepository;
import com.compilou.regex.services.PaymentService;
import com.compilou.regex.services.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/webhook/stripe")
@Tag(name = "Stripe", description = "Endpoints for Managing Payment With Stripe")
public class StripeWebhookController {

    @Value("${stripe.api.webHookKey}")
    private String webhookSecret;
    private StripeService stripeService;
    private PaymentService paymentService;
    private UserRepository userRepository;

    private StripeWebhookController(){}

    @Autowired
    public StripeWebhookController(StripeService stripeService,
                                   PaymentService paymentService,
                                   UserRepository userRepository) {
        this.stripeService = stripeService;
        this.paymentService = paymentService;
        this.userRepository = userRepository;
    }

    @PostMapping
    @Operation(summary = "Webhook to view Stripe events in real time", description = "Webhook to view Stripe events in real time",
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
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            Event event = stripeService.constructEvent(payload, sigHeader);

            if ("payment_intent.succeeded".equals(event.getType())) {
                Optional<StripeObject> paymentIntentObject = event.getDataObjectDeserializer().getObject();
                if (paymentIntentObject.isPresent()) {
                    Object stripeObject = paymentIntentObject.get();
                    if (stripeObject instanceof PaymentIntent) {
                        PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                        String paymentIntentId = paymentIntent.getId();

                        Long userId = paymentService.getUserIdByPaymentIntent(paymentIntentId);
                        if (userId != null) {
                            Optional<User> optionalUser = userRepository.findById(userId);
                            if (optionalUser.isPresent()) {
                                User user = optionalUser.get();
                                user.activateCustomerRole();
                                userRepository.save(user);
                                return ResponseEntity.status(HttpStatus.OK).body("Webhook received successfully");
                            } else {
                                throw new RuntimeException("Usuário não encontrado com ID: " + userId);
                            }
                        } else {
                            throw new RuntimeException("Não foi possível encontrar o usuário associado ao paymentIntentId: " + paymentIntentId);
                        }
                    } else {
                        return ResponseEntity.status(HttpStatus.OK).body("Unhandled event type");
                    }
                } else {
                    return ResponseEntity.status(HttpStatus.OK).body("PaymentIntent object not found in event");
                }
            } else {
                return ResponseEntity.status(HttpStatus.OK).body("Unhandled event type");
            }
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid webhook payload");
        }
    }
}