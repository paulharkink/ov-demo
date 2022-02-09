package com.example.ovticket.payment;

import javax.annotation.Nullable;

public interface PurchaseValidator {

    boolean isValid(@Nullable ProofOfPurchase proofOfPurchase);

}
