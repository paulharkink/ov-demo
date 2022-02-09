package com.example.ovticket.payment;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = GoogleInAppPurchaseProof.class, name = "Google InApp Purchase"),
        @JsonSubTypes.Type(value = IDealPurchaseProof.class, name = "iDeal")
})
public interface ProofOfPurchase {

    PurchaseType getType();

}
