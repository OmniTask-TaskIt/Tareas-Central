package com.taskit.mstareas.Infrastucture.Outbound.Messaging.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AitaTaskEventDTO {

    @JsonProperty("event_id")
    private String eventId;

    @JsonProperty("event_type")
    @Builder.Default
    private String eventType = "TaskCreatedEvent";

    @JsonProperty("occurred_at")
    private String occurredAt;

    private AitaTaskPayloadDTO task;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AitaTaskPayloadDTO {
        @JsonProperty("task_id")
        private String taskId;

        @JsonProperty("task_name")
        private String taskName;

        @JsonProperty("task_description")
        private String taskDescription;

        @JsonProperty("task_status")
        private String taskStatus;

        @JsonProperty("task_owner")
        private String taskOwner;

        @JsonProperty("task_assignee")
        private String taskAssignee;

        @JsonProperty("creation_date")
        private String creationDate;

        private String category;

        private long price;

        @JsonProperty("report_reason")
        private String reportReason;

        @JsonProperty("dispute_reason")
        private String disputeReason;

        @JsonProperty("owner_approval")
        private boolean ownerApproval;

        @JsonProperty("assignee_approval")
        private boolean assigneeApproval;
    }
}