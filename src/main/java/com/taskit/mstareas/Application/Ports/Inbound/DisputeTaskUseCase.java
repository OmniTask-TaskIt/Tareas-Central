package com.taskit.mstareas.Application.Ports.Inbound;

import com.taskit.mstareas.Domain.Model.Task;

public interface DisputeTaskUseCase {
    Task disputeTask(String taskId, String reason);
}
