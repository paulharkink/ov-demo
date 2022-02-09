package com.example.ovticket.payment;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class IDealPurchaseProofValidator implements PurchaseValidator {

    private static final String EXPECTED_RESULT_CODE = "Authorised";

    @Override
    public boolean isValid(ProofOfPurchase proofOfPurchase) {
        if (proofOfPurchase instanceof IDealPurchaseProof) {
            IDealPurchaseProof iDealPurchaseProof = (IDealPurchaseProof) proofOfPurchase;
            return StringUtils.equals(iDealPurchaseProof.getResultCode(), EXPECTED_RESULT_CODE)
                    && StringUtils.isNumeric(iDealPurchaseProof.getPspReference());
        }
        return false;
    }
}
