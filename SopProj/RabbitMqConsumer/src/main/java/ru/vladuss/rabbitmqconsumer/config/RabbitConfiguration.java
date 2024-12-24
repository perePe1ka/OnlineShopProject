package ru.vladuss.rabbitmqconsumer.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

    public static final String ORDER_STATUS_QUEUE = "goto-grps-queue";
    public static final String ORDER_STATUS_EXCHANGE = "goto-grps-exchange";
    public static final String ORDER_STATUS_ROUTING_KEY = "goto.grps.key";

    public static final String AUDIT_QUEUE = "audit-log-queue";
    public static final String FANOUT_EXCHANGE = "audit-fanout-exchange";

    @Bean
    public Queue orderStatusQueue() {
        return new Queue(ORDER_STATUS_QUEUE, false);
    }

    @Bean
    public Queue auditLogQueue() {
        return new Queue(AUDIT_QUEUE, false);
    }

    @Bean
    public Exchange orderStatusExchange() {
        return new TopicExchange(ORDER_STATUS_EXCHANGE);
    }

    @Bean
    public FanoutExchange auditFanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    public Binding orderStatusBinding(Queue orderStatusQueue, Exchange orderStatusExchange) {
        return BindingBuilder.bind(orderStatusQueue)
                .to(orderStatusExchange)
                .with(ORDER_STATUS_ROUTING_KEY)
                .noargs();
    }

    @Bean
    public Binding auditBinding(Queue auditLogQueue, FanoutExchange auditFanoutExchange) {
        return BindingBuilder.bind(auditLogQueue).to(auditFanoutExchange);
    }
}

