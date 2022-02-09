package com.example.ovticket.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;

@Service
@Slf4j
public class DelegatingPurchaseValidator implements PurchaseValidator{

    private final GoogleInAppPurchaseValidator googleInAppPurchaseValidator;
    private final IDealPurchaseProofValidator iDealPurchaseProofValidator;

    public DelegatingPurchaseValidator(GoogleInAppPurchaseValidator googleInAppPurchaseValidator, IDealPurchaseProofValidator iDealPurchaseProofValidator) {
        this.googleInAppPurchaseValidator = googleInAppPurchaseValidator;
        this.iDealPurchaseProofValidator = iDealPurchaseProofValidator;
    }

    @Override
    public boolean isValid(@Nullable ProofOfPurchase proofOfPurchase) {
        boolean isValid;
        if  (proofOfPurchase == null) {
            return false;
        }
        switch (proofOfPurchase.getType()) {
            case IDEAL:
                isValid = iDealPurchaseProofValidator.isValid(proofOfPurchase);
                break;
            case GoogleIAP:
                isValid = googleInAppPurchaseValidator.isValid(proofOfPurchase);
                break;
            default:
                log.warn("Unsupported purchase type {}", proofOfPurchase.getType());
                isValid = false;
        }
        return isValid;
    }
}
