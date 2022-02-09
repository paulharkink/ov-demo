package com.example.ovticket.payment;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@JsonPropertyOrder({"type", "resultCode", "pspReference"})
public class IDealPurchaseProof implements ProofOfPurchase {

    private final String resultCode;
    private final String pspReference;

    @Override
    public PurchaseType getType() {
        return PurchaseType.IDEAL;
    }
}
