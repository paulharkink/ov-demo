package com.example.ovticket.payment;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;

@Service
public class GoogleInAppPurchaseValidator implements PurchaseValidator {

    private final String expectedProductId;

    public GoogleInAppPurchaseValidator(@Value("${payment.google.productId:com.example.ovticket}") String expectedProductId) {
        this.expectedProductId = expectedProductId;
    }

    @Override
    public boolean isValid(@Nullable ProofOfPurchase proofOfPurchase) {
        if (proofOfPurchase instanceof GoogleInAppPurchaseProof) {
            GoogleInAppPurchaseProof googleInAppPurchaseProof = (GoogleInAppPurchaseProof) proofOfPurchase;
            return StringUtils.equals(expectedProductId, googleInAppPurchaseProof.getProductId()) &&
                    StringUtils.isNotEmpty(googleInAppPurchaseProof.getToken());
        }
        return false;
    }
}
