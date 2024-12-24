package ru.vladuss.auditlog.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitAuditConfiguration {

    public static final String AUDIT_QUEUE = "audit-log-queue";
    public static final String ORDER_STATUS_EXCHANGE = "audit-fanout-exchange";

    @Bean
    public Queue auditQueue() {
        return new Queue(AUDIT_QUEUE, false);
    }

    @Bean
    public FanoutExchange orderStatusExchange() {
        return new FanoutExchange(ORDER_STATUS_EXCHANGE);
    }

    @Bean
    public Binding auditBinding(Queue auditQueue, FanoutExchange orderStatusExchange) {
        return BindingBuilder.bind(auditQueue).to(orderStatusExchange);
    }
}

