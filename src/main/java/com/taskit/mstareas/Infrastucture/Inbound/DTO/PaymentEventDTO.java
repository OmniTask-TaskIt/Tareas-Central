package com.taskit.mstareas.Infrastucture.Inbound.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PaymentEventDTO(
        @JsonProperty("event-id") String eventId,
        @JsonProperty("event-type") String eventType,
        @JsonProperty("occurred-at") String occurredAt,
        @JsonProperty("transaction-id") String transactionId,
        @JsonProperty("task-id") String taskId,
        @JsonProperty("payer-id") String payerId,
        @JsonProperty("payee-id") String payeeId,
        @JsonProperty("amount") Long amount,
        @JsonProperty("currency") String currency,
        @JsonProperty("reason") String reason) {
}