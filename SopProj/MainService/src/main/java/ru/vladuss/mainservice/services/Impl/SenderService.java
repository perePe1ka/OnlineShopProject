package ru.vladuss.mainservice.services.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vladuss.contracts.dtos.OrdersStatusUpdateDto;
import ru.vladuss.mainservice.config.RabbitConfiguration;

import static ru.vladuss.mainservice.config.RabbitConfiguration.ORDER_STATUS_EXCHANGE;
import static ru.vladuss.mainservice.config.RabbitConfiguration.ORDER_STATUS_ROUTING_KEY;

@Service
public class SenderService {
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public SenderService(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Для заказа статусы заказа
     */
    public void sendUpdateOfOrderStatus(OrdersStatusUpdateDto update) {
        try {
            String message = objectMapper.writeValueAsString(update);

            rabbitTemplate.convertAndSend(RabbitConfiguration.ORDER_STATUS_EXCHANGE,
                    RabbitConfiguration.ORDER_STATUS_ROUTING_KEY,
                    message);

            rabbitTemplate.convertAndSend(RabbitConfiguration.FANOUT_EXCHANGE, "", message);

            System.out.println("Отправлено сообщение о статусе заказа: " + message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}


