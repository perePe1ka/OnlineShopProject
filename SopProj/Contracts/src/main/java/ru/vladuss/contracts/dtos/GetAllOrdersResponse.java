package ru.vladuss.contracts.dtos;

import java.util.List;

public class GetAllOrdersResponse {
    private List<OrderRequest> orders;

    public GetAllOrdersResponse() {
    }

    public GetAllOrdersResponse(List<OrderRequest> orders) {
        this.orders = orders;
    }

    public List<OrderRequest> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderRequest> orders) {
        this.orders = orders;
    }
}
