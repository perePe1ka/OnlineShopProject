package ru.vladuss.contracts.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.vladuss.contracts.dtos.GetAllProductsResponse;
import ru.vladuss.contracts.dtos.ProductDto;
import ru.vladuss.contracts.dtos.ProductRequest;
import ru.vladuss.contracts.dtos.ProductResponse;
import ru.vladuss.contracts.exceptions.NotFoundUuidException;


import java.util.List;
import java.util.UUID;

@Tag(name = "Product")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Успешная обработка запроса"),
        @ApiResponse(responseCode = "400", description = "Ошибка валидации"),
        @ApiResponse(responseCode = "404", description = "Товар не найден"),
        @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
})
public interface ProductApi {
    String PREFIX = "/api/products";

    @PostMapping(PREFIX + "/create")
    @Operation(summary = "Создание нового товара")
    ResponseEntity<ProductResponse> createProduct(@RequestBody ProductDto product) throws NotFoundUuidException;

    @PatchMapping(PREFIX + "/add/{productUuid}")
    @Operation(summary = "Добавление количества товара")
    ResponseEntity<String> addProduct(@PathVariable UUID productUuid, @RequestParam int quantity);

    @GetMapping(PREFIX + "/{productUUID}")
    @Operation(summary = "Получение информации о товаре")
    ResponseEntity<ProductRequest> getProduct(@PathVariable UUID productUUID) throws NotFoundUuidException;;

    @GetMapping(PREFIX + "/")
    @Operation(summary = "Получение списка всех товаров")
    ResponseEntity<CollectionModel<ProductResponse>>  getAllProducts();

    @PatchMapping(PREFIX + "/update")
    @Operation(summary = "Обновление товара")
    ResponseEntity<EntityModel<ProductResponse>> editProduct(@RequestBody ProductDto product) throws NotFoundUuidException;;

    @DeleteMapping(PREFIX + "/delete/{productUUID}")
    @Operation(summary = "Удаление товара")
    ResponseEntity<Void> deleteProduct(@PathVariable UUID productUUID);
}

