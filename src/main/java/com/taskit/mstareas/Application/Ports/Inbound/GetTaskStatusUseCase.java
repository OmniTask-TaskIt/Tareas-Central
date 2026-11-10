package com.taskit.mstareas.Application.Ports.Inbound;

import com.taskit.mstareas.Infrastucture.Inbound.DTO.TaskCompletionStatusDTO;

public interface GetTaskStatusUseCase {
    TaskCompletionStatusDTO getTaskStatus(String taskId);
}
