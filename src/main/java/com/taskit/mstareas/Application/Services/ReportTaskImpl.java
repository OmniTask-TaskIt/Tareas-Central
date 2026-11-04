package com.taskit.mstareas.Application.Services;

import com.taskit.mstareas.Application.Ports.Inbound.ReportTaskUseCase;
import com.taskit.mstareas.Domain.Model.Task;
import com.taskit.mstareas.Domain.Model.TaskStatus;
import com.taskit.mstareas.Domain.Repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportTaskImpl implements ReportTaskUseCase {

    private final TaskRepository taskRepository;

    @Override
    public Task reportTask(String taskId, String reason) {
        if (taskId == null || reason == null || reason.isEmpty()) {
            throw new IllegalArgumentException("Task ID and reason are required");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));

        task.setTaskStatus(TaskStatus.REPORTED);
        task.setReportReason(reason);

        return taskRepository.save(task);
    }
}
