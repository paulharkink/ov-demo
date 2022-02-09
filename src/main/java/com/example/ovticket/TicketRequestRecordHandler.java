package com.example.ovticket;

import com.amazonaws.services.kinesis.clientlibrary.exceptions.InvalidStateException;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.ShutdownException;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.types.InitializationInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ProcessRecordsInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ShutdownInput;
import com.amazonaws.services.kinesis.model.Record;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;

@Slf4j
@Service
public class TicketRequestRecordHandler implements IRecordProcessor {

    private final ObjectReader reader;
    private final TicketService ticketService;

    public TicketRequestRecordHandler(ObjectMapper objectMapper, TicketService ticketService) {
        reader = objectMapper
                .reader()
                .forType(TicketRequest.class);
        this.ticketService = ticketService;
    }

    @Override
    public void initialize(InitializationInput initializationInput) {
        MDC.put("shard", initializationInput.getShardId());
        log.info("Initializing worker: {}, from checkpoint {}", initializationInput, initializationInput.getPendingCheckpointSequenceNumber());
    }

    @Override
    public void processRecords(ProcessRecordsInput processRecordsInput) {
        log.debug("Received records {}", processRecordsInput);
        if (!processRecordsInput.getTimeSpentInCache().isZero()) {
            log.warn("Waited {} before picking up this batch of {} messages",
                    processRecordsInput.getTimeSpentInCache(),
                    processRecordsInput.getRecords().size());
        }
        processRecordsInput.getRecords()
                .forEach(this::processRecord);
        log.info("Forcing a checkpoint");
        try {
            processRecordsInput.getCheckpointer().checkpoint();
        } catch (InvalidStateException | ShutdownException e) {
            log.error("Unable to create checkpoint", e);
        }
    }

    @Override
    public void shutdown(ShutdownInput shutdownInput) {
        try {
            log.info("Shutdown requested: {}", shutdownInput);
            shutdownInput.getCheckpointer().checkpoint();
        } catch (ShutdownException | InvalidStateException e) {
            log.error("Unable to set checkpoint on shutdown request!", e);
        }
    }

    public void processRecord(Record record) {
        log.debug("Processing record {}", record);
        Optional.ofNullable(record)
                .map(Record::getData)
                .map(ByteBuffer::array)
                .map(this::readTicketRequest)
                .ifPresentOrElse(ticketService::handleRequest, () -> log.error("No TicketRequest to process"));
    }


    @Nullable
    private TicketRequest readTicketRequest(byte[] array) {
        try {
            return reader.readValue(array);
        } catch (IOException e) {
            log.warn("Unable to read kinesis record", e);
            return null;
        }
    }
}
