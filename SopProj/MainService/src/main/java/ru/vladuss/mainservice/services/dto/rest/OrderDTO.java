package ru.vladuss.mainservice.services.dto.rest;

import ru.vladuss.mainservice.constants.Status;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class OrderDTO {
    private UUID uuid;
    private String customerName;
    private String customerAddress;
    private LocalDateTime orderDate;
    private long orderCost;
    private Status status;
    private Set<ProductDTO> products;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public long getOrderCost() {
        return orderCost;
    }

    public void setOrderCost(long orderCost) {
        this.orderCost = orderCost;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductDTO> products) {
        this.products = products;
    }
}
