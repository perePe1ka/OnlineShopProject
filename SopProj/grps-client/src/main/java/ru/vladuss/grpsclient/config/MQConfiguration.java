package ru.vladuss.grpsclient.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfiguration {
    @Value("${rabbitmq.queue.name}")
    private String queueName;

    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;

    public static final String AUDIT_QUEUE = "audit-log-queue";
    public static final String FANOUT_EXCHANGE = "audit-fanout-exchange";

    @Bean
    public Queue notificationQueue() {
        return new Queue(queueName, false, false, false);
    }

    @Bean
    public Queue auditLogQueue() {
        return new Queue(AUDIT_QUEUE, false);
    }

    @Bean
    public Exchange exchange() {
        return new TopicExchange(exchangeName, false, false);
    }

    @Bean
    public FanoutExchange auditFanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue notificationQueue, Exchange exchange) {
        return BindingBuilder.bind(notificationQueue).to(exchange).with("order.notifications").noargs();
    }

    @Bean
    public Binding auditBinding(Queue auditLogQueue, FanoutExchange auditFanoutExchange) {
        return BindingBuilder.bind(auditLogQueue).to(auditFanoutExchange);
    }
}