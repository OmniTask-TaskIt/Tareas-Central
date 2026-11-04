package com.taskit.mstareas.Domain.Model;

import lombok.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class Task {
    private String taskId;
    private String taskName;
    private String taskDescription;
    private TaskStatus taskStatus;
    private String taskOwner;
    private String taskAssignee;
    private LocalDateTime creationDate;
    private TaskCategory category;
    private long price;
    private String reportReason;
    private String disputeReason;
    private boolean ownerApproval;
    private boolean assigneeApproval;
    // private Location location;
}
