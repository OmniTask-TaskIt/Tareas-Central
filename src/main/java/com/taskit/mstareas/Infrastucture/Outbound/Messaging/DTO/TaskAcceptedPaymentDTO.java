package com.taskit.mstareas.Infrastucture.Outbound.Messaging.DTO;

public record TaskAcceptedPaymentDTO(String taskId, String taskOwner, String taskAssignee, long amount) {}
