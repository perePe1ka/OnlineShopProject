package ru.vladuss.mainservice.services;

import org.springframework.stereotype.Service;
import ru.vladuss.contracts.dtos.ProductDto;
import ru.vladuss.mainservice.entity.Product;

import java.util.List;
import java.util.UUID;

@Service
public interface IProductService<T, U> {

    void createProduct(T product);

    void addProduct(UUID productUuid, int quantity);

    void deleteByUUID(U uuid);

    ProductDto findByUUID(U uuid);

    List<ProductDto> findAll();

    void editProduct(T updatingProduct);
}