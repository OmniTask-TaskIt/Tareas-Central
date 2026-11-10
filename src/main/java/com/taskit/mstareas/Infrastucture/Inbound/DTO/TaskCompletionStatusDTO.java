package com.taskit.mstareas.Infrastucture.Inbound.DTO;

import com.taskit.mstareas.Domain.Model.TaskStatus;

public record TaskCompletionStatusDTO(
    String taskId,
    TaskStatus status,
    boolean ownerApproved,
    boolean assigneeApproved,
    String ownerId,
    String assigneeId
) {}
