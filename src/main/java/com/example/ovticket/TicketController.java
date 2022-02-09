package com.example.ovticket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public TicketResponse handleTicketRequest(@RequestBody TicketRequest ticketRequest) {
        log.info("Request: {}", ticketRequest);
        return ticketService.respondToRequest(ticketRequest);
    }
}
