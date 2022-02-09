package com.example.ovticket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@JsonTest
public class TicketRequestSerializerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldDeserializeTicketRequest() throws JsonProcessingException {

        TicketRequest deserialized = objectMapper.reader()
                .forType(TicketRequest.class)
                .readValue(TicketRequests.TICKET_REQUEST_JSON);

        assertThat(deserialized, notNullValue());
        assertThat(deserialized.getReturnToken(), is(true));
        assertThat(deserialized, is(TicketRequests.TICKET_REQUEST));
    }
}
