package com.example.ovticket;

import com.example.ovticket.payment.ProofOfPurchase;
import com.example.ovticket.transport.TransitLocation;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.time.ZonedDateTime;

@Data
@Builder
@Jacksonized
public class TicketRequest {

    @Builder.Default
    private final Boolean returnToken = true;
    @Builder.Default
    private final Boolean returnQR = true;

    private final String pointOfSale;

    @Builder.Default
    private String returnChannel = "JSON";

    private final ProofOfPurchase proofOfPurchase;

    private final ZonedDateTime validFrom;

    private final ZonedDateTime validUntil;

    private final TransitLocation start;

    private final TransitLocation destination;

}
