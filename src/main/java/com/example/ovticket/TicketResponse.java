package com.example.ovticket;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class TicketResponse implements Response {

    private final String code;

    private final String message;

    private final String channel;

    private final String jwt;

    private final byte[] qr;

}
