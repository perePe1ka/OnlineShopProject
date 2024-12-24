package ru.vladuss.contracts.dtos;

import java.util.List;

public class GetAllProductsResponse {
    private List<ProductResponse> products;

    public GetAllProductsResponse(List<ProductResponse> products) {
        this.products = products;
    }

    public List<ProductResponse> getProducts() {
        return products;
    }

    public void setProducts(List<ProductResponse> products) {
        this.products = products;
    }

    public GetAllProductsResponse() {
    }
}
