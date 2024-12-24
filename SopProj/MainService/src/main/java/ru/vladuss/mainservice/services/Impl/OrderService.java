package ru.vladuss.mainservice.services.Impl;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.vladuss.contracts.dtos.OrderDto;
import ru.vladuss.contracts.dtos.OrdersStatusUpdateDto;
import ru.vladuss.mainservice.constants.Status;
import ru.vladuss.mainservice.entity.Orders;
import ru.vladuss.mainservice.entity.Product;
import ru.vladuss.mainservice.repositories.IOrderRepository;
import ru.vladuss.mainservice.repositories.IProductRepository;
import ru.vladuss.mainservice.services.IOrderService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService implements IOrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final ModelMapper modelMapper;
    private final IOrderRepository orderRepository;
    private final IProductRepository productRepository;

    private final SenderService senderService;

    @Autowired
    public OrderService(IOrderRepository orderRepository, IProductRepository productRepository, SenderService senderService, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.senderService = senderService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void addOrder(OrderDto orderDto) {
        boolean isProductInStock = orderDto.getProducts().stream()
                .allMatch(product -> {
                    Product product1 = productRepository.findById(product.getUuid()).orElseThrow(() ->
                            new IllegalArgumentException("Не нашли продукт: " + product.getName()));
                    return product1.getStockQuantity() >= 1;
                });

        if (isProductInStock) {
            orderDto.getProducts().forEach(product -> {
                Product product1 = productRepository.findById(product.getUuid()).get();
                int newStock = product1.getStockQuantity() - 1;
                product1.setStockQuantity(newStock);

                if (newStock == 0) {
                    product1.setInStock(false);
                }
                productRepository.saveAndFlush(product1);
            });

            Orders order = convertToEntity(orderDto);
            order.setStatus(ru.vladuss.mainservice.constants.Status.NO_STATUS);
            orderRepository.saveAndFlush(order);
            logger.info("Заказ {} успешно создан со статусом NO_STATUS.", order.getUuid());
        } else {
            logger.warn("Попытка создания заказа не удалась: один или несколько товаров отсутствуют на складе.");
        }
    }


    @Override
    public void deleteByUUID(UUID uuid) {
        orderRepository.deleteById(uuid);
        logger.info("Заказ с UUID {} был удалён.", uuid);
    }

    @Override
    public Optional<Orders> findByUUID(UUID uuid) {
        Optional<Orders> existingOrders = orderRepository.findById(uuid);
        if (existingOrders.isPresent()) {
            logger.info("Заказ {} найден.", uuid);
            return Optional.of(existingOrders.get());
        } else {
            logger.warn("Заказ с UUID {} не найден.", uuid);
            return null;
        }
    }


    @Override
    public List<Orders> findAll() {
        logger.info("Получение всех заказов.");
        return orderRepository.findAll();
    }

    @Override
    public void updateOrderStatus() {
        List<Orders> orders = orderRepository.findAll();
        Random random = new Random();
        for (Orders order : orders) {
            Status currStatus = order.getStatus();
            if (currStatus == Status.DELIVERED) {
                break;
            }
            Status nextStatus = getNextStatus(currStatus);
            if (nextStatus != null) {
                int delay = random.nextInt(25) + 5;
                order.setStatus(nextStatus);
                order.setTimeOfSwapStatus(Instant.now());
                orderRepository.saveAndFlush(order);

                logger.info("Статус заказа {} обновлён на {} с задержкой {} секунд.", order.getUuid(), nextStatus, delay);

                OrdersStatusUpdateDto updateDto = new OrdersStatusUpdateDto(
                        order.getUuid(),
                        nextStatus.name(),
                        delay,
                        order.getOrderCost(),
                        order.getProducts().stream().map(Product::getName).collect(Collectors.toSet()),
                        order.isDiscountWas()
                );
                senderService.sendUpdateOfOrderStatus(updateDto);
            } else {
                logger.warn("Неизвестный статус для заказа {}: {}", order.getUuid(), currStatus);
            }
        }
    }

    private Status getNextStatus(Status currentStatus) {
        switch (currentStatus) {
            case NO_STATUS:
                return Status.ORDERED;
            case ORDERED:
                return Status.CONFIRMED;
//            case CONFIRMED: return Status.ASSEMBLING;
//            case ASSEMBLING: return Status.PACKED;
//            case PACKED: return Status.SHIPPED;
//            case SHIPPED: return Status.IN_TRANSIT;
//            case IN_TRANSIT: return Status.AWAITING_PICKUP;
            case CONFIRMED:
                return Status.AWAITING_PICKUP;
            case AWAITING_PICKUP:
                return Status.DELIVERED;
            default:
                return null;
        }
    }

    @Override
    public void editOrder(OrderDto orders) {
        Optional<Orders> existingOrder = orderRepository.findById(orders.getUuid());

        if (existingOrder.isPresent()) {
            Orders currOrders = existingOrder.get();
            currOrders.setCustomerName(orders.getCustomerName());
            currOrders.setCustomerAddress(orders.getCustomerAddress());
            currOrders.setOrderDate(orders.getOrderDate());
            currOrders.setOrderCost(orders.getOrderCost());

            orderRepository.saveAndFlush(currOrders);
            logger.info("Заказ {} был обновлён.", orders.getUuid());
        } else {
            logger.warn("Заказ с UUID {} не найден, невозможно обновить.", orders.getUuid());
        }
    }

    @Scheduled(fixedDelay = 3000)
    public void scheduleUpdateOrderStatus() {
        updateOrderStatus();
    }



    private Orders convertToEntity(OrderDto dto) {
        Orders order = modelMapper.map(dto, Orders.class);
        order.setStatus(ru.vladuss.mainservice.constants.Status.valueOf(dto.getStatus().name()));
        return order;
    }

    private OrderDto convertToDto(Orders entity) {
        OrderDto dto = modelMapper.map(entity, OrderDto.class);
        dto.setStatus(ru.vladuss.contracts.dtos.Status.valueOf(entity.getStatus().name()));
        return dto;
    }


}
