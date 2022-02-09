package com.example.ovticket.payment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JsonTest
public class DelegatingPurchaseValidatorTest {

    private DelegatingPurchaseValidator delegatingPurchaseValidator;

    @BeforeEach
    public void setup(){
        delegatingPurchaseValidator = new DelegatingPurchaseValidator(
                new GoogleInAppPurchaseValidator("com.example.ovticket.android"),
                new IDealPurchaseProofValidator()
        );
    }

    @Test
    public void shouldRecognizeValidGoogleProof() {
        // given
        ProofOfPurchase googleInAppPurchaseProof = new GoogleInAppPurchaseProof("com.example.ovticket.android", "non-empty");

        // when
        assertTrue(delegatingPurchaseValidator.isValid(googleInAppPurchaseProof));
    }

    @Test
    public void shouldRecognizeInvalidGoogleProof() {
        // given
        ProofOfPurchase googleInAppPurchaseProof = new GoogleInAppPurchaseProof("com.example.ovticket", "non-empty");

        // when
        assertFalse(delegatingPurchaseValidator.isValid(googleInAppPurchaseProof));
    }

    @Test
    public void shouldRecognizeValidIDealProof() {
        // given
        ProofOfPurchase iDealPurchaseProof = new IDealPurchaseProof("Authorised", "1234");

        // when
        assertTrue(delegatingPurchaseValidator.isValid(iDealPurchaseProof));
    }

    @Test
    public void shouldRecognizeInvalidIDealProof() {
        // given
        ProofOfPurchase iDealPurchaseProof = new IDealPurchaseProof("Unauthorised", "non-empty");

        // when
        assertFalse(delegatingPurchaseValidator.isValid(iDealPurchaseProof));
    }
}