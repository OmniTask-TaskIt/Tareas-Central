package com.taskit.mstareas.Infrastucture.Outbound.Messaging.DTO;

public record CreateChatRequestDTO(
        String taskId,
        String taskName,
        String taskOwner,
        String taskAssignee
) {
}