package ru.vladuss.mainservice.services.dto.rest;

import java.util.Set;
import java.util.UUID;

public class OrderCreateDTO {
    private String customerName;
    private String customerAddress;
    private Set<UUID> productIds;

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

    public Set<UUID> getProductIds() {
        return productIds;
    }

    public void setProductIds(Set<UUID> productIds) {
        this.productIds = productIds;
    }
}
