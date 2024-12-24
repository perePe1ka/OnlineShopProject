package ru.vladuss.contracts.dtos;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.HashMap;
import java.util.Map;

public class ProductRequest extends RepresentationModel<ProductRequest> {
    private ProductDto product;

    private final Map<String, Map<String, String>> action = new HashMap<>();

    public ProductRequest(ProductDto product) {
        this.product = product;
    }

    public ProductDto getProduct() {
        return product;
    }

    public void setProduct(ProductDto product) {
        this.product = product;
    }

    public Map<String, Map<String, String>> getAction() {
        return action;
    }

    public ProductRequest() {

    }

    public void addProduct(String name, String method, WebMvcLinkBuilder link) {
        Map<String, String> actionField = new HashMap<>();
        actionField.put("href", link.withSelfRel().getHref());
        actionField.put("method", method);
        action.put(name, actionField);
    }
}
