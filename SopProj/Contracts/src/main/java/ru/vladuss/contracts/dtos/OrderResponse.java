package ru.vladuss.contracts.dtos;


import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public class OrderResponse {
    private UUID uuid;
    private LocalDateTime orderDate;
    private String customerName;
    private String customerAddress;
    private long orderCost;
    private Status status;
    private Instant timeOfSwapStatus;

    public OrderResponse() {
    }

    public OrderResponse(UUID uuid, LocalDateTime orderDate, String customerName, String customerAddress, long orderCost, Status status, Instant timeOfSwapStatus) {
        this.uuid = uuid;
        this.orderDate = orderDate;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.orderCost = orderCost;
        this.status = status;
        this.timeOfSwapStatus = timeOfSwapStatus;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Instant getTimeOfSwapStatus() {
        return timeOfSwapStatus;
    }

    public void setTimeOfSwapStatus(Instant timeOfSwapStatus) {
        this.timeOfSwapStatus = timeOfSwapStatus;
    }
}
