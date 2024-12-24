package ru.vladuss.mainservice.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.vladuss.contracts.controllers.OrderApi;
import ru.vladuss.contracts.dtos.GetAllOrdersResponse;
import ru.vladuss.contracts.dtos.OrderDto;
import ru.vladuss.contracts.dtos.OrderRequest;
import ru.vladuss.mainservice.entity.Orders;
import ru.vladuss.mainservice.services.IOrderService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class OrderController implements OrderApi {

    private final ModelMapper modelMapper;
    private final IOrderService<Orders, UUID> orderService;

    @Autowired
    public OrderController(IOrderService<Orders, UUID> orderService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity<OrderRequest> addOrder(@RequestBody OrderDto orders) {
        orderService.addOrder(orders);
        OrderRequest orderRequest = new OrderRequest(orders);


        return ResponseEntity.ok().body(orderRequest);
    }


    @Override
    public ResponseEntity<OrderRequest> getOrder(@PathVariable UUID orderUUID) {
        Optional<Orders> orderOpt = orderService.findByUUID(orderUUID);

        if (orderOpt.isPresent()) {
            Orders order = orderOpt.get();
            OrderDto orderDto = convertToDto(order);
            OrderRequest orderRequest = new OrderRequest(orderDto);

            // Добавление гиперссылок
            orderRequest.add(linkTo(methodOn(OrderController.class).getOrder(orderDto.getUuid())).withSelfRel());
            orderRequest.add(linkTo(methodOn(OrderController.class).getAllOrders()).withRel("orders"));

            // Добавление ссылок для обновления и удаления
            orderRequest.addOrders("update", "PUT", linkTo(methodOn(OrderController.class).editOrder(orderDto)).withSelfRel());
            orderRequest.addOrders("delete", "DELETE", linkTo(methodOn(OrderController.class).deleteOrder(orderDto.getUuid())).withSelfRel());

            return ResponseEntity.ok().body(orderRequest);
        } else {
            return ResponseEntity.notFound().build();
        }
    }




    @Override
    public ResponseEntity<List<GetAllOrdersResponse>> getAllOrders() {
        List<GetAllOrdersResponse> ordersResponse = orderService.findAll().stream()
                .map(order -> {
                    OrderDto orderDto = convertToDto(order);

                    OrderRequest orderRequest = new OrderRequest(orderDto);
                    orderRequest.add(linkTo(methodOn(OrderController.class).getOrder(orderDto.getUuid())).withSelfRel());
                    orderRequest.add(linkTo(methodOn(OrderController.class).getAllOrders()).withRel("devices"));

                    orderRequest.addOrders("update", "PUT", linkTo(methodOn(OrderController.class).editOrder(orderDto)).withRel("update"));
                    orderRequest.addOrders("delete", "DELETE", linkTo(methodOn(OrderController.class).deleteOrder(orderDto.getUuid())).withRel("delete"));
                    return new GetAllOrdersResponse(Collections.singletonList(orderRequest));
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(ordersResponse);
    }


    @Override
    public ResponseEntity<OrderRequest> editOrder(@RequestBody OrderDto orders) {
        orderService.editOrder(orders);
        OrderRequest orderRequest = new OrderRequest(orders);


        return ResponseEntity.ok(orderRequest);
    }

    @Override
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID orderUUID) {
        Optional<Orders> orderOpt = orderService.findByUUID(orderUUID);
        if (orderOpt.isPresent()) {
            orderService.deleteByUUID(orderUUID);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private OrderDto convertToDto(Orders order) {
        return modelMapper.map(order, OrderDto.class);
    }

}
