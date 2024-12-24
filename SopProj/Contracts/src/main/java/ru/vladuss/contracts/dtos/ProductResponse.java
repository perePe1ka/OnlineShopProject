package ru.vladuss.contracts.dtos;

import java.util.UUID;

public class ProductResponse {
    private UUID uuid;
    private String name;
    private String description;
    private Double price;
    private Integer stockQuantity;
    private Boolean inStock;

    public ProductResponse(UUID uuid, String name, String description, Double price, Integer stockQuantity, Boolean inStock) {
        this.uuid = uuid;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.inStock = inStock;
    }

    public ProductResponse() {
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

}
