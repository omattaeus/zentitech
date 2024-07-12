package com.compilou.regex.controllers;

import com.compilou.regex.mapper.request.StripeRequest;
import com.compilou.regex.models.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Stripe", description = "Endpoints for Managing Payment With Stripe")
public class StripeController {

    @Value("${stripe.api.publicKey}")
    private String publicKey;

    @GetMapping("/teste")
    @Operation(summary = "Return Page Product", description = "Return Page Product",
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
    public String home(Model model){
        model.addAttribute("request", new StripeRequest());
        return "principal/product";
    }

    @PostMapping
    @Operation(summary = "Set Atribbutes From Checkout", description = "Set Atribbutes From Checkout",
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
    public String home(@ModelAttribute("email") String email, Model model){
        StripeRequest request = new StripeRequest();
        request.setEmail(email);
        request.setAmount("14.90");
        request.setProductName("Plano Starter");

        model.addAttribute("request", request);

        return "redirect:/checkout";
    }

    @GetMapping("/checkout")
    @Operation(summary = "Show Checkout Page", description = "Show Checkout Page",
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