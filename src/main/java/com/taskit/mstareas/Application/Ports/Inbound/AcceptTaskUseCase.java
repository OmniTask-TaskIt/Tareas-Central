package com.taskit.mstareas.Application.Ports.Inbound;

import com.taskit.mstareas.Domain.Model.Task;

public interface AcceptTaskUseCase {
    Task acceptTask(String taskId, String taskAssignee);
}
