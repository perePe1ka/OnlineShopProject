package ru.vladuss.grpsbackend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vladuss.grpsbackend.entity.Orders;
import ru.vladuss.grpsbackend.entity.Product;
import ru.vladuss.grpsbackend.repository.OrderRepository;
import ru.vladuss.grpsbackend.repository.ProductRepository;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderValidationService.class);

    private final ProductRepository productRepository;

    private final OrderRepository orderRepository;


    @Autowired
    public OrderService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }



    public boolean validateOrder(UUID orderUuid, double orderCost, boolean discountWas) {
        LOGGER.info("Проверка заказа по идентификатору: {}", orderUuid);
        Orders order = orderRepository.findByUuid(orderUuid);
        if (order == null) {
            LOGGER.warn("Order not found with ID: {}", orderUuid);
            return false;
        }

        List<Product> products = productRepository.findByOrdersUuid(orderUuid);
        if (products.isEmpty()) {
            LOGGER.warn("Заказ не найден по идентификатору: {}", orderUuid);
            return false;
        }

        double expectedCost = products.stream()
                .mapToDouble(product -> product.getPrice() * (discountWas ? 0.9 : 1.0))
                .sum();

        boolean isValid = Math.abs(expectedCost - orderCost) < 0.01;
        LOGGER.info("Результат проверки идентификатора заказа {}: {}", orderUuid, isValid);
        return isValid;
    }

    public void updateOrderCost(UUID orderUuid, double newCost) {
        LOGGER.info("Обновление стоимости заказа для идентификатора {}: НОВАЯ ЦЕНА {}", orderUuid, newCost);
        Orders order = orderRepository.findByUuid(orderUuid);
        if (order != null) {
            order.setOrderCost((long) newCost);
            orderRepository.save(order);
            LOGGER.info("Стоимость заказа обновлена для идентификатора {}", orderUuid);
        } else {
            LOGGER.warn("Заказ не найден для обновления стоимости: ID {}", orderUuid);
        }
    }

    public double calculateCorrectedCost(UUID orderUuid, boolean discountWas) {
        LOGGER.info("Расчет скорректированной стоимости для идентификатора заказа {}", orderUuid);
        List<Product> products = productRepository.findByOrdersUuid(orderUuid);
        double correctedCost = products.stream()
                .mapToDouble(product -> product.getPrice() * (discountWas ? 0.9 : 1.0))
                .sum();
        LOGGER.info("Скорректированная стоимость для идентификатора заказа {}: {}", orderUuid, correctedCost);
        return correctedCost;
    }
}
