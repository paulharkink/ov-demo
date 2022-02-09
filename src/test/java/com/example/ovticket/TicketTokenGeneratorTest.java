package com.example.ovticket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

class TicketTokenGeneratorTest {

    TicketTokenGenerator ticketTokenGenerator;

    @BeforeEach
    public void setup() {
        ticketTokenGenerator = new TicketTokenGenerator("secret");
    }

    @Test
    public void shouldGenerateTicket() {
        String token = ticketTokenGenerator.generateToken(TicketRequests.TICKET_REQUEST);

        assertThat(token, notNullValue());
    }
}