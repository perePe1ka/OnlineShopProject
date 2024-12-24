//package ru.vladuss.grpsbackend.config;
//
//import org.springframework.amqp.core.*;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class MQConfiguration {
//
//    @Value("${rabbitmq.queue.name}")
//    private String queueName;
//
//    @Value("${rabbitmq.exchange.name}")
//    private String exchangeName;
//
//    @Bean
//    public Queue notificationQueue() {
//        return new Queue(queueName, false, false, false);
//    }
//
//    @Bean
//    public Exchange exchange() {
//        return new TopicExchange(exchangeName, false, false);
//    }
//
//    @Bean
//    public Binding binding(Queue notificationQueue, Exchange exchange) {
//        return BindingBuilder.bind(notificationQueue).to(exchange).with("order.notifications").noargs();
//    }
//}
