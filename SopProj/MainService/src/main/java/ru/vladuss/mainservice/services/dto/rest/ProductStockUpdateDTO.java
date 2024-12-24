package ru.vladuss.mainservice.services.dto.rest;

import java.util.UUID;

public class ProductStockUpdateDTO {
    private UUID productUuid;
    private int quantity;

    public UUID getProductUuid() {
        return productUuid;
    }

    public void setProductUuid(UUID productUuid) {
        this.productUuid = productUuid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
