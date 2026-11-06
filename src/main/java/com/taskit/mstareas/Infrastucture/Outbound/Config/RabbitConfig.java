package com.taskit.mstareas.Infrastucture.Outbound.Config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;




@Configuration
public class RabbitConfig {

    // Exchanges
    public static final String TASK_EXCHANGE = "task.exchange";
    public static final String CHAT_EXCHANGE = "chat.exchange";
    public static final String PAYMENT_EXCHANGE = "payment.exchange";

    // Queues
    public static final String TASK_CREATED_QUEUE = "task.created.queue";
    public static final String CHAT_CREATE_QUEUE = "chat.create.queue";
    public static final String CHAT_DELETE_QUEUE = "chat.delete.queue";
    public static final String PAYMENT_NOTIFY_QUEUE = "payment.notify.queue";

    // Routing Keys
    public static final String ROUTING_KEY_TASK_CREATED = "task.created";
    public static final String ROUTING_KEY_CHAT_CREATE = "chat.create";
    public static final String ROUTING_KEY_CHAT_DELETE = "chat.delete";
    public static final String ROUTING_KEY_PAYMENT_NOTIFY = "payment.task.accepted";

    // Exchange Beans
    @Bean
    public TopicExchange taskExchange() {
        return new TopicExchange(TASK_EXCHANGE);
    }

    @Bean
    public DirectExchange chatExchange() {
        return new DirectExchange(CHAT_EXCHANGE);
    }

    @Bean
    public DirectExchange paymentExchange() {
        return new DirectExchange(PAYMENT_EXCHANGE);
    }

    // Queue Beans
    @Bean
    public Queue taskCreatedQueue() {
        return new Queue(TASK_CREATED_QUEUE, true);
    }

    @Bean
    public Queue chatCreateQueue() {
        return new Queue(CHAT_CREATE_QUEUE, true);
    }

    @Bean
    public Queue chatDeleteQueue() {
        return new Queue(CHAT_DELETE_QUEUE, true);
    }

    @Bean
    public Queue paymentNotifyQueue() {
        return new Queue(PAYMENT_NOTIFY_QUEUE, true);
    }

    // Bindings
    @Bean
    public Binding taskCreatedBinding(Queue taskCreatedQueue, TopicExchange taskExchange) {
        return BindingBuilder.bind(taskCreatedQueue).to(taskExchange).with(ROUTING_KEY_TASK_CREATED);
    }

    @Bean
    public Binding chatCreateBinding(Queue chatCreateQueue, DirectExchange chatExchange) {
        return BindingBuilder.bind(chatCreateQueue).to(chatExchange).with(ROUTING_KEY_CHAT_CREATE);
    }

    @Bean
    public Binding chatDeleteBinding(Queue chatDeleteQueue, DirectExchange chatExchange) {
        return BindingBuilder.bind(chatDeleteQueue).to(chatExchange).with(ROUTING_KEY_CHAT_DELETE);
    }

    @Bean
    public Binding paymentNotifyBinding(Queue paymentNotifyQueue, DirectExchange paymentExchange) {
        return BindingBuilder.bind(paymentNotifyQueue).to(paymentExchange).with(ROUTING_KEY_PAYMENT_NOTIFY);
    }

    // Template & Converter Configuration
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}