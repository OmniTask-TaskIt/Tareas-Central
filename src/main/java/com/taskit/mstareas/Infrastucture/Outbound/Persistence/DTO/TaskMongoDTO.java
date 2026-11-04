package com.taskit.mstareas.Infrastucture.Outbound.Persistence.DTO;

import com.taskit.mstareas.Domain.Model.TaskCategory;
import com.taskit.mstareas.Domain.Model.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "tasks")
public class TaskMongoDTO {
    @Id
    private String taskId;
    private String taskName;
    private String taskDescription;
    private String taskOwner;
    private String taskAssignee;
    private TaskStatus taskStatus;
    private long price;
    private TaskCategory category;
    private LocalDateTime creationDate;
    private String reportReason;
    private String disputeReason;
    private boolean ownerApproval;
    private boolean assigneeApproval;
}
