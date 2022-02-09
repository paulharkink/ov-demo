package com.example.ovticket;

import com.example.ovticket.payment.PurchaseValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    private final PurchaseValidator purchaseValidator;
    private final TicketTokenGenerator ticketTokenGenerator;
    private final QRGenerator qrGenerator;

    public TicketService(@Qualifier("delegatingPurchaseValidator") PurchaseValidator purchaseValidator,
                         TicketTokenGenerator ticketTokenGenerator,
                         QRGenerator qrGenerator) {
        this.purchaseValidator = purchaseValidator;
        this.ticketTokenGenerator = ticketTokenGenerator;
        this.qrGenerator = qrGenerator;
    }

    public void handleRequest(TicketRequest ticketRequest) {
        TicketResponse response = respondToRequest(ticketRequest);


    }

    public TicketResponse respondToRequest(TicketRequest ticketRequest) {
        TicketResponse.TicketResponseBuilder responseBuilder = TicketResponse.builder();
        if (purchaseValidator.isValid(ticketRequest.getProofOfPurchase())) {
            responseBuilder.code("200");
            responseBuilder.message("Success");

            String token = ticketTokenGenerator.generateToken(ticketRequest);
            responseBuilder.jwt(token);
            responseBuilder.qr(qrGenerator.render(token));

        } else {
            responseBuilder.message("Payment could not be verified");
            responseBuilder.code("402"); // whatever
        }
        return responseBuilder
                .channel(ticketRequest.getReturnChannel())
                .build();
    }


}
