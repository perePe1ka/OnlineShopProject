package ru.vladuss.mainservice.services.dto.rest;

import java.util.UUID;

public class OrderListDTO {
    private UUID uuid;
    private String customerName;
    private String customerAddress;
    private long orderCost;

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

    public long getOrderCost() {
        return orderCost;
    }

    public void setOrderCost(long orderCost) {
        this.orderCost = orderCost;
    }
}
