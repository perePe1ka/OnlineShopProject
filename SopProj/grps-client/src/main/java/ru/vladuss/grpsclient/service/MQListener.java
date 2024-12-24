package ru.vladuss.grpsclient.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vladuss.grpsbackend.OrderRequest;
import ru.vladuss.grpsbackend.OrderResponse;
import ru.vladuss.grpsbackend.Status;
import ru.vladuss.grpsclient.config.GrpsClient;
import ru.vladuss.grpsclient.config.MQConfiguration;

@Service
public class MQListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(MQListener.class);
    private final GrpsClient grpsClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RabbitTemplate rabbitTemplate;


    @Autowired
    public MQListener(GrpsClient grpsClient, RabbitTemplate rabbitTemplate) {
        this.grpsClient = grpsClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "goto-grps-queue")
    public void receiveMessage(String message) {
        try {
            LOGGER.info("Получено сообщение: {}", message);
            JsonNode jsonNode = objectMapper.readTree(message);

            String orderId = jsonNode.get("uuid").asText();
            if (orderId == null || orderId.isEmpty()) {
                throw new IllegalArgumentException("UUID is missing or empty in RabbitMQ message");
            }

            String statusString = jsonNode.get("status").asText();
            int delay = jsonNode.get("delay").asInt();
            double orderCost = jsonNode.get("orderCost").asDouble();
            boolean discountWas = jsonNode.get("discountWas").asBoolean();

            Status status = Status.valueOf(statusString);

            OrderRequest request = OrderRequest.newBuilder()
                    .setOrderId(orderId.toString())
                    .setOrderCost((float) orderCost)
                    .setIsDiscountWas(discountWas)
                    .setStatus(status)
                    .build();

            LOGGER.info("Отправка запроса с Order ID: {}", orderId);
            OrderResponse response = grpsClient.validateOrderCost(request);
            LOGGER.info("Ответ от gRPC-сервиса: {}", response.getMessage());
            sendMessageToNotifier("Ответ от gRPC: " + response.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("Ошибка преобразования данных: {}", e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Ошибка обработки сообщения: ", e);
        }
    }

    public void sendMessageToNotifier(String message) {
        rabbitTemplate.convertAndSend("order-notification-exchange", "order.notifications", message);
        rabbitTemplate.convertAndSend(MQConfiguration.FANOUT_EXCHANGE, "", message);
        LOGGER.info("Отправлено сообщение = {} ", message);
    }

}

