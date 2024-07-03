package com.compilou.regex.controllers;

import com.compilou.regex.mapper.request.StripeRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/payment")
public class StripeController {

    @Value("${stripe.api.publicKey}")
    private String publicKey;

    @GetMapping("/teste")
    public String home(Model model){
        model.addAttribute("request", new StripeRequest());
        return "principal/product";
    }

    @PostMapping
    public String home(@ModelAttribute("email") String email, Model model){
        StripeRequest request = new StripeRequest();
        request.setEmail(email);
        request.setAmount("14.90");
        request.setProductName("Plano Starter");

        model.addAttribute("request", request);

        return "redirect:/checkout";
    }

    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, HttpSession session) {

        String email = (String) session.getAttribute("userEmail");

        String amount = "R$14.90";
        String productName = "Plano Starter";

        model.addAttribute("publicKey", publicKey);
        model.addAttribute("amount", amount);
        model.addAttribute("email", email);
        model.addAttribute("productName", productName);

        return "principal/checkout";
    }
}