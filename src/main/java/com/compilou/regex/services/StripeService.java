package com.compilou.regex.services;

import com.compilou.regex.services.auth.UserService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StripeService {

    @Value("${stripe.api.webHookKey}")
    private String webhookSecret;
    private final UserService userService;
    private final PaymentService paymentService;

    @Autowired
    public StripeService(UserService userService, PaymentService paymentService) {
        this.userService = userService;
        this.paymentService = paymentService;
    }

    public Event constructEvent(String payload, String sigHeader) throws StripeException {
        try {
            return Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            e.getMessage();
        }
        return null;
    }

    public void handlePaymentSuccess(String payload, String sigHeader) throws StripeException {
        Event event = constructEvent(payload, sigHeader);
        if (event != null) {
            String eventId = event.getId();
            if ("payment_intent.succeeded".equals(event.getType())) {
                Optional<StripeObject> optionalPaymentIntent = event.getDataObjectDeserializer().getObject();
                if (optionalPaymentIntent.isPresent()) {
                    StripeObject stripeObject = optionalPaymentIntent.get();
                    if (stripeObject instanceof PaymentIntent) {
                        PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                        String paymentIntentId = paymentIntent.getId();
                        Long userId = paymentService.getUserIdByPaymentIntent(paymentIntentId);
                        if (userId != null) {
                            userService.activateCustomerRole(userId);
                        } else {
                            throw new RuntimeException("Não foi possível encontrar o usuário associado ao paymentIntentId: " + paymentIntentId);
                        }
                    } else {
                        throw new RuntimeException("Objeto recebido não é um PaymentIntent");
                    }
                } else {
                    throw new RuntimeException("Objeto PaymentIntent não encontrado no evento");
                }
            } else {
                throw new RuntimeException("Evento não suportado: " + event.getType());
            }
        } else {
            throw new RuntimeException("Evento inválido ou não reconhecido");
        }
    }
}