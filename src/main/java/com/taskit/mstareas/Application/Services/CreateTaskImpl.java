package com.taskit.mstareas.Application.Services;

import com.taskit.mstareas.Application.Ports.Inbound.CreateTaskUserCase;
import com.taskit.mstareas.Application.Ports.Outbound.TaskEventPublisher; 
import com.taskit.mstareas.Domain.Model.Task;
import com.taskit.mstareas.Domain.Model.TaskCategory;
import com.taskit.mstareas.Domain.Model.TaskStatus;
import com.taskit.mstareas.Domain.Repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; 
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateTaskImpl implements CreateTaskUserCase {
    
    private final TaskRepository taskRepository;
    private final TaskEventPublisher taskEventPublisher;

    @Override
    public Task createTask(String taskOwner, String taskName,
                           String taskDescription, long price, TaskCategory category) {

        if (taskOwner == null || taskName == null ||
                taskDescription == null || price <= 0) {
            throw new IllegalArgumentException("Invalid task data");
        }

        Task task = Task.builder()
                .taskId(UUID.randomUUID().toString())
                .taskOwner(taskOwner)
                .taskName(taskName)
                .taskDescription(taskDescription)
                .price(price)
                .creationDate(LocalDateTime.now())
                .taskStatus(TaskStatus.ACTIVE)
                .category(category)
                .build();
        
        Task savedTask = taskRepository.save(task);

        //  AITA 
        try {
            taskEventPublisher.notifyTaskCreatedForAita(savedTask);
        } catch (Exception e) {
            log.error("Fallo al notificar la creación de la tarea a AITA: {}", e.getMessage());
        }

        return savedTask;
    }
}