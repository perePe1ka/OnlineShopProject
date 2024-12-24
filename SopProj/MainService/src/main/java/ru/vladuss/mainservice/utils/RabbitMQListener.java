package ru.vladuss.mainservice.utils;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vladuss.mainservice.services.Impl.NotificationService;

@Service
public class RabbitMQListener {

    @Autowired
    private NotificationService notificationService;

    @RabbitListener(queues = "mainservice-queue")
    public void listen(String message) {
        System.out.println("Получено сообщение: " + message);
        notificationService.sendNotification(message);
    }
}
