package ru.vladuss.mainservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.vladuss.mainservice.constants.Status;
import ru.vladuss.mainservice.entity.Orders;
import ru.vladuss.mainservice.entity.Product;
import ru.vladuss.mainservice.repositories.IOrderRepository;
import ru.vladuss.mainservice.repositories.IProductRepository;

import java.time.Instant;
import java.time.LocalDateTime;

@Component
public class DataInit implements CommandLineRunner {

    private final IOrderRepository orderRepository;

    private final IProductRepository productRepository;

    @Autowired
    public DataInit(IOrderRepository orderRepository, IProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        Orders orders1 = new Orders(LocalDateTime.now(), "Customer 5", "Address 5", Status.NO_STATUS, 1000, Instant.now(), false);
        Orders orders2 = new Orders(LocalDateTime.now(), "Customer 6", "Address 6", Status.NO_STATUS, 2000, Instant.now(), false);
        Orders orders3 = new Orders(LocalDateTime.now(), "Customer 7", "Address 7", Status.NO_STATUS, 3000, Instant.now(), false);
        Orders orders4 = new Orders(LocalDateTime.now(), "Customer 8", "Address 8", Status.NO_STATUS, 4000, Instant.now(), false);

        orderRepository.saveAndFlush(orders1);
        orderRepository.saveAndFlush(orders2);
        orderRepository.saveAndFlush(orders3);
        orderRepository.saveAndFlush(orders4);


        Product product1 = new Product("Product 5", "Description 5", 10.0, 1, orders1, true);
        Product product2 = new Product("Product 6", "Description 6", 20.0, 2, orders2, true);
        Product product3 = new Product("Product 7", "Description 7", 30.0, 3, orders3, true);
        Product product4 = new Product("Product 8", "Description 8", 40.0, 4, orders4, true);

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
        productRepository.save(product4);
    }
}

