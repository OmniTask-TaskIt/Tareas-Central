package com.taskit.mstareas.Infrastucture.Inbound.DTO;

import com.taskit.mstareas.Domain.Model.TaskCategory;

public record CreateTaskRequestDTO(
        String taskOwner,
        String taskName,
        String taskDescription,
        long price,
        TaskCategory category
) {
}