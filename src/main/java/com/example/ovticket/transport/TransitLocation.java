package com.example.ovticket.transport;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;

import java.util.Set;

@Data
@Builder
@Jacksonized
public class TransitLocation {

    private final String id;

    @Singular
    private final Set<TransportType> transportTypes;

    private final String name;
    private final String region;
    private final String provence;
    private final String country;

    private final double longitude;
    private final double latitude;

}
