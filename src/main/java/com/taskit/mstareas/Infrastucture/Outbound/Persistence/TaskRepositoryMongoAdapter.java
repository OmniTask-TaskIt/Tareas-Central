package com.taskit.mstareas.Infrastucture.Outbound.Persistence;

import com.taskit.mstareas.Domain.Model.Task;
import com.taskit.mstareas.Domain.Model.TaskCategory;
import com.taskit.mstareas.Domain.Model.TaskStatus;
import com.taskit.mstareas.Domain.Repository.TaskRepository;
import com.taskit.mstareas.Infrastucture.Outbound.Persistence.DTO.TaskMongoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TaskRepositoryMongoAdapter implements TaskRepository {

    private final TaskRepositoryMongo taskRepositoryMongo;

    @Override
    public Task save(Task task) {
        TaskMongoDTO dto = TaskMongoDTO.builder()
                .taskId(task.getTaskId())
                .taskName(task.getTaskName())
                .taskDescription(task.getTaskDescription())
                .taskOwner(task.getTaskOwner())
                .taskAssignee(task.getTaskAssignee())
                .taskStatus(task.getTaskStatus())
                .price(task.getPrice())
                .category(task.getCategory())
                .creationDate(task.getCreationDate())
                .reportReason(task.getReportReason())
                .disputeReason(task.getDisputeReason())
                .ownerApproval(task.isOwnerApproval())
                .assigneeApproval(task.isAssigneeApproval())
                .build();

        TaskMongoDTO saved = taskRepositoryMongo.save(dto);

        return mapToDomain(saved);
    }

    @Override
    public Optional<Task> findById(String taskId) {
        return taskRepositoryMongo.findById(taskId)
                .map(this::mapToDomain);
    }

    @Override
    public void deleteById(String taskId) {
        taskRepositoryMongo.deleteById(taskId);
    }

    @Override
    public boolean existsById(String taskId) {
        return taskRepositoryMongo.existsById(taskId);
    }

    @Override
    public List<Task> findByFilters(TaskCategory category, Long minPrice, Long maxPrice, String keyword) {
        String categoryStr = (category != null) ? category.name() : null;
        return taskRepositoryMongo.findByFilters(categoryStr, minPrice, maxPrice, keyword)
                .stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findByCategories(List<TaskCategory> categories) {
        return taskRepositoryMongo.findByCategoryIn(categories)
                .stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findByOwner(String ownerId) {
        return taskRepositoryMongo.findByTaskOwner(ownerId)
                .stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findByAssignee(String assigneeId) {
        return taskRepositoryMongo.findByTaskAssignee(assigneeId)
                .stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> findByAssigneeAndStatus(String assigneeId, TaskStatus status) {
        return taskRepositoryMongo.findByTaskAssigneeAndTaskStatus(assigneeId, status)
                .stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    private Task mapToDomain(TaskMongoDTO dto) {
        return Task.builder()
                .taskId(dto.getTaskId())
                .taskName(dto.getTaskName())
                .taskDescription(dto.getTaskDescription())
                .taskOwner(dto.getTaskOwner())
                .taskAssignee(dto.getTaskAssignee())
                .taskStatus(dto.getTaskStatus())
                .creationDate(dto.getCreationDate())
                .category(dto.getCategory())
                .price(dto.getPrice())
                .reportReason(dto.getReportReason())
                .disputeReason(dto.getDisputeReason())
                .ownerApproval(dto.isOwnerApproval())
                .assigneeApproval(dto.isAssigneeApproval())
                .build();
    }
}