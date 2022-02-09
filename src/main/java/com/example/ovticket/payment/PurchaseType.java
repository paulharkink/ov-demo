package com.example.ovticket.payment;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PurchaseType {

    IDEAL("iDeal"),
    GoogleIAP("Google InApp Purchase"),
    AppleIAP("Apple In App Purchase");

    @JsonValue
    public final String name;
}
