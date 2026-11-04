package com.taskit.mstareas.Application.Ports.Inbound;

import com.taskit.mstareas.Domain.Model.Task;

public interface ReportTaskUseCase {
    Task reportTask(String taskId, String reason);
}
