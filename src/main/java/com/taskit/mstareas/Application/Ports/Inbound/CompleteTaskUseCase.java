package com.taskit.mstareas.Application.Ports.Inbound;

import com.taskit.mstareas.Domain.Model.Task;

public interface CompleteTaskUseCase {
    Task approveCompletion(String taskId, String userId);
}
