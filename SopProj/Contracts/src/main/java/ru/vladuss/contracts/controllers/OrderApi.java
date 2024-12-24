package ru.vladuss.contracts.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vladuss.contracts.dtos.GetAllOrdersResponse;
import ru.vladuss.contracts.dtos.OrderDto;
import ru.vladuss.contracts.dtos.OrderRequest;


import java.util.List;
import java.util.UUID;

@Tag(name = "Order")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Успешная обработка запроса"),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
        @ApiResponse(responseCode = "404", description = "Заказ не найден"),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
})
public interface OrderApi {
    String PREFIX = "/api/orders";

    @PostMapping(PREFIX + "/add")
    @Operation(summary = "Создание нового заказа")
    ResponseEntity<OrderRequest> addOrder(@RequestBody OrderDto orders);

    @GetMapping(PREFIX + "/{orderUUID}")
    @Operation(summary = "Получение информации о заказе")
    ResponseEntity<OrderRequest> getOrder(@PathVariable UUID orderUUID);

    @GetMapping(PREFIX + "/")
    @Operation(summary = "Получение списка всех заказов")
    ResponseEntity<List<GetAllOrdersResponse>> getAllOrders();

    @PatchMapping(PREFIX + "/update")
    @Operation(summary = "Обновление заказа")
    ResponseEntity<OrderRequest> editOrder(@RequestBody OrderDto orders);

    @DeleteMapping(PREFIX + "/delete/{orderUUID}")
    @Operation(summary = "Удаление заказа")
    ResponseEntity<Void> deleteOrder(@PathVariable UUID orderUUID);
}

