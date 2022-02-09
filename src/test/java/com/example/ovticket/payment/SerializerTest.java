package com.example.ovticket.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.notNullValue;

@JsonTest
public class SerializerTest {

    private static final String GOOGLE_PURCHASE_JSON = "{\"type\":\"Google InApp Purchase\",\"productId\":\"com.example.ovticket.android\",\"token\":\"1234\"}";
    private static final String IDEAL_PURCHASE_JSON = "{\"type\":\"iDeal\",\"resultCode\":\"Authorised\",\"pspReference\":\"1234\"}";

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldSerializeGoogleProof() throws JsonProcessingException {
        // given
        ProofOfPurchase googleInAppPurchaseProof = GoogleInAppPurchaseProof
                .builder()
                .productId("com.example.ovticket.android")
                .token("1234")
                .build();

        // when
        String serialized = objectMapper.writer().writeValueAsString(googleInAppPurchaseProof);

        // then
        assertThat(serialized, is(GOOGLE_PURCHASE_JSON));
    }

    @Test
    public void shouldSerializeIDealProof() throws JsonProcessingException {
        // given
        ProofOfPurchase iDealPurchaseProof = new IDealPurchaseProof("Authorised", "1234");

        // when
        String serialized = objectMapper.writer().writeValueAsString(iDealPurchaseProof);

        // then
        assertThat(serialized, is(IDEAL_PURCHASE_JSON));
    }

    @Test
    public void shouldSerializeEnum() throws JsonProcessingException {
        // when
        String serialized = objectMapper.writer().writeValueAsString(PurchaseType.GoogleIAP);

        Object deserialized = objectMapper.reader().forType(PurchaseType.class)
                .readValue(serialized);

        // then
        assertThat(serialized, notNullValue());
        assertThat(deserialized, notNullValue());
        assertThat(deserialized, is(PurchaseType.GoogleIAP));
        assertThat(serialized, is("\"Google InApp Purchase\""));
    }

    @Test
    public void shouldDeserializeGoogleProof() throws JsonProcessingException {
        // when
        ProofOfPurchase proofOfPurchase = objectMapper
                .reader()
                .forType(ProofOfPurchase.class)
                .readValue(GOOGLE_PURCHASE_JSON);

        // then
        assertThat(proofOfPurchase, notNullValue());
        assertThat(proofOfPurchase, isA(GoogleInAppPurchaseProof.class));
    }

    @Test
    public void shouldDeserializeIDealProof() throws JsonProcessingException {
        // when
        ProofOfPurchase proofOfPurchase = objectMapper
                .reader()
                .forType(ProofOfPurchase.class)
                .readValue(IDEAL_PURCHASE_JSON);

        // then
        assertThat(proofOfPurchase, notNullValue());
        assertThat(proofOfPurchase, isA(IDealPurchaseProof.class));
    }
}
