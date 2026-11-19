package com.taskit.mstareas.Infrastucture.Outbound.Messaging.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TaskStatusChangedEventDTO(
                @JsonProperty("event-id") String eventId,
                @JsonProperty("occurred-at") String occurredAt,
                @JsonProperty("task-id") String taskId,
                @JsonProperty("task-name") String taskName,
                @JsonProperty("previous-status") String previousStatus,
                @JsonProperty("task-status") String taskStatus,
                @JsonProperty("task-owner") String taskOwner,
                @JsonProperty("task-assignee") String taskAssignee,
                @JsonProperty("category") String category,
                @JsonProperty("price") long price,
                @JsonProperty("currency") String currency) {
}