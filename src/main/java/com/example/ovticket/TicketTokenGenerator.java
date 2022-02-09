package com.example.ovticket;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.chrono.ChronoZonedDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class TicketTokenGenerator {

    private final String secret;

    public TicketTokenGenerator(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    public String generateToken(TicketRequest ticketRequest) {
        JwtBuilder builder = Jwts.builder()
                .setSubject("ticket");
        Optional.ofNullable(ticketRequest.getValidFrom())
                .map(ChronoZonedDateTime::toInstant)
                .map(Date::from)
                .ifPresent(builder::setNotBefore);
        Optional.ofNullable(ticketRequest.getValidUntil())
                .map(ChronoZonedDateTime::toInstant)
                .map(Date::from)
                .ifPresent(builder::setExpiration);

        builder.claim("start", ticketRequest.getStart());
        builder.claim("destination", ticketRequest.getDestination());

        return builder.setIssuer("OV Ticket Demo System")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
