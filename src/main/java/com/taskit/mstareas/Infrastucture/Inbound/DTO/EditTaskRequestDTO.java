package com.taskit.mstareas.Infrastucture.Inbound.DTO;

import com.taskit.mstareas.Domain.Model.TaskCategory;

public record EditTaskRequestDTO(
        String taskName,
        String taskDescription,
        long price,
        TaskCategory category
) {
}
