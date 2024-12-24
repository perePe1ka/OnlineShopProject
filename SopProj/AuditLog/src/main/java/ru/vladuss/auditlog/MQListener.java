package ru.vladuss.auditlog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.vladuss.auditlog.config.RabbitAuditConfiguration;

@Service
public class MQListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(MQListener.class);

    @RabbitListener(queues = RabbitAuditConfiguration.AUDIT_QUEUE)
    public void logMessage(String message) {
        LOGGER.info("Аудит лог: Получено сообщение: {}", message);

    }
}
