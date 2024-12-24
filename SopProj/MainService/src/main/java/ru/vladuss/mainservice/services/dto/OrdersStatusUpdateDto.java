package ru.vladuss.mainservice.services.dto;

import ru.vladuss.mainservice.constants.Status;
import ru.vladuss.mainservice.entity.Product;

import java.util.Set;
import java.util.UUID;
public class OrdersStatusUpdateDto extends BaseDto {
    private String status;
    private int delay;
    private long orderCost;
    private Set<String> products;

    public OrdersStatusUpdateDto(UUID uuid, String status, int delay, long orderCost, Set<String> products) {
        super(String.valueOf(uuid));
        this.status = status;
        this.delay = delay;
        this.orderCost = orderCost;
        this.products = products;
    }


    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getDelay() { return delay; }
    public void setDelay(int delay) { this.delay = delay; }

    public long getOrderCost() { return orderCost; }
    public void setOrderCost(long orderCost) { this.orderCost = orderCost; }

    public Set<String> getProducts() { return products; }
    public void setProducts(Set<String> products) { this.products = products; }


    @Override
    public String toString() {
        return "OrderStatusUpdate{" +
                "orderId=" + getUuid() +
                ", status='" + status + '\'' +
                ", delay=" + delay +
                ", orderCost=" + orderCost +
                ", products=" + products +
                '}';
    }
}
