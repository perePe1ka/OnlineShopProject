package ru.vladuss.mainservice.controllers.dataFetchers;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vladuss.contracts.dtos.OrderDto;
import ru.vladuss.mainservice.entity.Orders;
import ru.vladuss.mainservice.services.IOrderService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@DgsComponent
public class OrderDataFetcher {
    private final IOrderService<Orders, UUID> orderService;
    private final ModelMapper modelMapper;

    @Autowired
    public OrderDataFetcher(IOrderService<Orders, UUID> orderService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }

    @DgsMutation
    public OrderDto addOrder(@InputArgument(name = "orders") OrderDto orderDto) {
        orderService.addOrder(orderDto);
        return orderDto;
    }

    @DgsQuery
    public OrderDto getOrder(@InputArgument(name = "orderUUID") String orderUUID) {
        UUID uuid = UUID.fromString(orderUUID);
        Optional<Orders> orderOpt = orderService.findByUUID(uuid);

        if (orderOpt.isPresent()) {
            return convertToDto(orderOpt.get());
        } else {
            throw new IllegalArgumentException("Order with UUID " + orderUUID + " not found.");
        }
    }

    @DgsQuery
    public List<OrderDto> getAllOrders() {
        List<Orders> orders = orderService.findAll();
        return orders.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @DgsMutation
    public OrderDto editOrder(@InputArgument(name = "orders") OrderDto orderDto) {
        orderService.editOrder(orderDto);
        return orderDto;
    }

    @DgsMutation
    public void deleteOrder(@InputArgument(name = "orderUUID") String orderUUID) {
        UUID uuid = UUID.fromString(orderUUID);
        orderService.deleteByUUID(uuid);
    }

    private OrderDto convertToDto(Orders order) {
        return modelMapper.map(order, OrderDto.class);
    }

    private Orders convertToEntity(OrderDto orderDto) {
        Orders orders = modelMapper.map(orderDto, Orders.class);
        orders.setStatus(ru.vladuss.mainservice.constants.Status.valueOf(orderDto.getStatus().name()));
        return orders;
    }
}


