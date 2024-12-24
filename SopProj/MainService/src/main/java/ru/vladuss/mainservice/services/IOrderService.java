package ru.vladuss.mainservice.services;

import org.springframework.stereotype.Service;
import ru.vladuss.contracts.dtos.OrderDto;
import ru.vladuss.mainservice.entity.Orders;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface IOrderService<T, U> {

    void addOrder(OrderDto orders);

    void deleteByUUID(UUID uuid);

    Optional<Orders> findByUUID(UUID uuid);

    List<Orders> findAll();

    void updateOrderStatus();

    void editOrder(OrderDto orders);
}