package ru.vladuss.rabbitmqconsumer.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vladuss.rabbitmqconsumer.config.RabbitConfiguration;
import ru.vladuss.rabbitmqconsumer.dtos.ConsumerOrdersStatusUpdateDto;

import static ru.vladuss.rabbitmqconsumer.config.RabbitConfiguration.ORDER_STATUS_EXCHANGE;
import static ru.vladuss.rabbitmqconsumer.config.RabbitConfiguration.ORDER_STATUS_ROUTING_KEY;

@Service
public class OrderStatusSenderService {
    private final RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper;

    @Autowired
    public OrderStatusSenderService(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendStateToGrps(ConsumerOrdersStatusUpdateDto orderDto) {
        try {
            String message = objectMapper.writeValueAsString(orderDto);
            rabbitTemplate.convertAndSend(ORDER_STATUS_EXCHANGE, ORDER_STATUS_ROUTING_KEY, message);
            rabbitTemplate.convertAndSend(RabbitConfiguration.FANOUT_EXCHANGE, "", message);
            System.out.println("Отправлено сообщение с айди " + orderDto.getUuid() +
                    ", флагом скидки " + orderDto.isDiscountWas() +
                    ", статусом " + orderDto.getStatus() +
                    ", и ценой " + orderDto.getOrderCost());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
