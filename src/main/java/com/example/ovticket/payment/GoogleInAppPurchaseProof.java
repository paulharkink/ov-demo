package com.example.ovticket.payment;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@JsonPropertyOrder({"type", "productId", "token"})
@Builder
@Jacksonized
public class GoogleInAppPurchaseProof implements ProofOfPurchase {

    private final String productId;
    private final String token;

    @Override
    public PurchaseType getType() {
        return PurchaseType.GoogleIAP;
    }

}
