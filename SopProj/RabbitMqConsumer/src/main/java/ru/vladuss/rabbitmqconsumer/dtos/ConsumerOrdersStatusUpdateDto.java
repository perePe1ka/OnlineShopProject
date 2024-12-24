package ru.vladuss.rabbitmqconsumer.dtos;

import java.util.Set;

public class ConsumerOrdersStatusUpdateDto {
    private String uuid;
    private String status;
    private int delay;
    private long orderCost;
    private Set<String> products;
    private boolean isDiscountWas;

    public ConsumerOrdersStatusUpdateDto() {
    }

    public ConsumerOrdersStatusUpdateDto(String uuid, String status, int delay, long orderCost, Set<String> products, boolean isDiscountWas) {
        this.uuid = uuid;
        this.status = status;
        this.delay = delay;
        this.orderCost = orderCost;
        this.products = products;
        this.isDiscountWas = isDiscountWas;
    }

    public boolean isDiscountWas() {
        return isDiscountWas;
    }

    public void setDiscountWas(boolean discountWas) {
        isDiscountWas = discountWas;
    }

    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getDelay() { return delay; }
    public void setDelay(int delay) { this.delay = delay; }

    public long getOrderCost() { return orderCost; }
    public void setOrderCost(long orderCost) { this.orderCost = orderCost; }

    public Set<String> getProducts() { return products; }
    public void setProducts(Set<String> products) { this.products = products; }
}
