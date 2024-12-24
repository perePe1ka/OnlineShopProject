package ru.vladuss.notifier;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.core.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NotifierApplication {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Bean
    public Queue mainServiceQueue() {
        return new Queue("mainservice-queue", false);
    }

    @RabbitListener(queues = "notifier-queue")
    public void listen(String message) {
        System.out.println("Сообщение из очереди Notifier: " + message);
        rabbitTemplate.convertAndSend("mainservice-queue", message);
    }

    public static void main(String[] args) {
        SpringApplication.run(NotifierApplication.class, args);
    }
}
