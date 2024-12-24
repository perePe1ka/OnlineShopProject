package ru.vladuss.contracts.dtos;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import java.util.HashMap;
import java.util.Map;

public class OrderRequest extends RepresentationModel<OrderRequest> {
    private OrderDto orders;

    private final Map<String, Map<String, String>> action = new HashMap<>();

    public OrderRequest(OrderDto orders) {
        this.orders = orders;
    }

    public OrderDto getOrders() {
        return orders;
    }

    public void setOrders(OrderDto orders) {
        this.orders = orders;
    }

    public Map<String, Map<String, String>> getAction() {
        return action;
    }

    public OrderRequest() {

    }
    public void addOrders(String name, String method, Link link) {
        Map<String, String> actionField = new HashMap<>();
        actionField.put("href", link.withSelfRel().getHref());
        actionField.put("method", method);
        action.put(name, actionField);
    }
}
