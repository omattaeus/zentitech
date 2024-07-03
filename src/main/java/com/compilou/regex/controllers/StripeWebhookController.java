package com.compilou.regex.controllers;

import com.compilou.regex.models.User;
import com.compilou.regex.repositories.UserRepository;
import com.compilou.regex.services.PaymentService;
import com.compilou.regex.services.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/webhook/stripe")
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