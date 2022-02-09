package com.example.ovticket;

import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorCheckpointer;
import com.amazonaws.services.kinesis.clientlibrary.types.ProcessRecordsInput;
import com.amazonaws.services.kinesis.model.Record;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.List;

import static com.example.ovticket.TicketRequests.TICKET_REQUEST;

@JsonTest
class TicketRequestHandlerTest {

    private TicketRequestRecordHandler ticketRequestProcessor;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private TicketService ticketService;

    private AutoCloseable openMocks;

    @BeforeEach
    public void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        ticketRequestProcessor = new TicketRequestRecordHandler(objectMapper, ticketService);
    }

    @AfterEach
    public void clean() throws Exception {
        openMocks.close();
    }


    @Test
    public void shouldProcessRecords() throws JsonProcessingException {
        // given
        Record record = new Record()
                .withData(ByteBuffer.wrap(objectMapper.writeValueAsBytes(TICKET_REQUEST)));

        ProcessRecordsInput recordsInput = new ProcessRecordsInput()
                .withRecords(List.of(record))
                .withCacheEntryTime(new Date().toInstant())
                .withCheckpointer(Mockito.mock(IRecordProcessorCheckpointer.class));

        // when
        ticketRequestProcessor.processRecords(recordsInput);
    }
}