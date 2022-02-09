package com.example.ovticket;

import com.example.ovticket.payment.GoogleInAppPurchaseProof;
import com.example.ovticket.transport.TransitLocation;
import com.example.ovticket.transport.TransportType;

import java.time.ZonedDateTime;

public interface TicketRequests {

    TicketRequest TICKET_REQUEST = TicketRequest
            .builder()
            .validFrom(ZonedDateTime.parse("2022-02-09T21:57:08.048+01:00[Europe/Amsterdam]"))
            .validUntil(ZonedDateTime.parse("2022-02-10T21:57:08.048+01:00[Europe/Amsterdam]"))
            .pointOfSale("androidId12356")
            .proofOfPurchase(GoogleInAppPurchaseProof
                    .builder()
                    .productId("com.example.ovticket.android")
                    .token("12345")
                    .build())
            .start(TransitLocation
                    .builder()
                    .transportType(TransportType.INTERCITY)
                    .transportType(TransportType.SPRINTER)
                    .transportType(TransportType.METRO)
                    .transportType(TransportType.TRAM)
                    .transportType(TransportType.BUS)
                    .id("AMS-WTC")
                    .name("Amsterdam Zuid")
                    .region("Amsterdam")
                    .provence("Noord Holland")
                    .country("NL")
                    .latitude(52.33974605103632)
                    .longitude(4.873348198634602)
                    .build())
            .destination(TransitLocation.builder()
                    .transportType(TransportType.SPRINTER)
                    .transportType(TransportType.METRO)
                    .id("AMS-DIEMEN-ZUID")
                    .name("Diemen Zuid")
                    .region("Amsterdam")
                    .provence("Noord Holland")
                    .country("NL")
                    .latitude(52.33330161262987)
                    .longitude(4.955470148538422)
                    .build())
            .build();

    String TICKET_REQUEST_JSON = """
            {
              "returnToken": true,
              "returnQR": true,
              "pointOfSale": "androidId12356",
              "returnChannel": "JSON",
              "proofOfPurchase": {
                "type": "Google InApp Purchase",
                "productId": "com.example.ovticket.android",
                "token": "12345"
              },
              "validFrom": "2022-02-09T21:57:08.048+01:00[Europe/Amsterdam]",
              "validUntil": "2022-02-10T21:57:08.048+01:00[Europe/Amsterdam]",
              "start": {
                "id": "AMS-WTC",
                "transportTypes": [
                  "INTERCITY",
                  "SPRINTER",
                  "METRO",
                  "TRAM",
                  "BUS"
                ],
                "name": "Amsterdam Zuid",
                "region": "Amsterdam",
                "provence": "Noord Holland",
                "country": "NL",
                "longitude": 4.873348198634602,
                "latitude": 52.33974605103632
              },
              "destination": {
                "id": "AMS-DIEMEN-ZUID",
                "transportTypes": [
                  "SPRINTER",
                  "METRO"
                ],
                "name": "Diemen Zuid",
                "region": "Amsterdam",
                "provence": "Noord Holland",
                "country": "NL",
                "longitude": 4.955470148538422,
                "latitude": 52.33330161262987
              }
            }
            """;
}
