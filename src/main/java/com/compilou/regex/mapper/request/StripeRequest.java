package com.compilou.regex.mapper.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StripeRequest {

    @NotBlank
    private String amount;

    @NotBlank
    private String productName;

    @NotBlank
    private String email;

    public Long convertToLong(String amount) throws NumberFormatException {
        if (amount == null || amount.isEmpty()) {
            throw new IllegalArgumentException("Amount cannot be null or empty");
        }

        String cleanedAmount = amount.replaceAll("[^\\d]", "");
        if (cleanedAmount.isEmpty()) {
            throw new NumberFormatException("Amount string is empty after cleaning");
        }

        return Long.parseLong(cleanedAmount);
    }
}