package ru.vladuss.rabbitmqconsumer;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RabbitMqConsumerApplication {

    public static final String OrderStatusQueueName = "order-status-queue";
    @Bean
    public Queue notificationQueue() {
        return new Queue(OrderStatusQueueName, false);
    }


    @RabbitListener(queues = OrderStatusQueueName)
    public void ListenNotification(String message) {
        System.out.println("Прочитано с очереди " + OrderStatusQueueName + " сообщение " + " " + message);
    }

    public static void main(String[] args) {
        System.setProperty("server.port", "8082");
        SpringApplication.run(RabbitMqConsumerApplication.class, args);
    }

}
