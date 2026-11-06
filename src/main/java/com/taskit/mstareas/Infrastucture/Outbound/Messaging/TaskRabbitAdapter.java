package com.taskit.mstareas.Infrastucture.Outbound.Messaging;

import com.taskit.mstareas.Application.Ports.Outbound.TaskEventPublisher;
import com.taskit.mstareas.Infrastucture.Outbound.Config.RabbitConfig;
import com.taskit.mstareas.Infrastucture.Outbound.Messaging.DTO.CreateChatRequestDTO;
import com.taskit.mstareas.Infrastucture.Outbound.Messaging.DTO.DeleteChatRequestDTO;
import com.taskit.mstareas.Infrastucture.Outbound.Messaging.DTO.TaskAcceptedPaymentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskRabbitAdapter implements TaskEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void createChat(String taskId, String taskName, String taskOwner, String taskAssignee) {
        CreateChatRequestDTO event = new CreateChatRequestDTO(
                taskId,
                taskName,
                taskOwner,
                taskAssignee
        );
        rabbitTemplate.convertAndSend(
                RabbitConfig.CHAT_EXCHANGE,
                RabbitConfig.ROUTING_KEY_CHAT_CREATE,
                event
        );
    }

    @Override
    public void deleteChat(String taskId) {
        DeleteChatRequestDTO event = new DeleteChatRequestDTO(taskId);
        rabbitTemplate.convertAndSend(
                RabbitConfig.CHAT_EXCHANGE,
                RabbitConfig.ROUTING_KEY_CHAT_DELETE,
                event
        );
    }

    @Override
    public void notifyTaskAccepted(String taskId, String taskOwner, String taskAssignee, long amount) {
        TaskAcceptedPaymentDTO event = new TaskAcceptedPaymentDTO(taskId, taskOwner, taskAssignee, amount);
        rabbitTemplate.convertAndSend(
                RabbitConfig.PAYMENT_EXCHANGE,
                RabbitConfig.ROUTING_KEY_PAYMENT_NOTIFY,
                event
        );
    }
}
