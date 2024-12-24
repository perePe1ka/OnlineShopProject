package ru.vladuss.grpsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.vladuss.grpsbackend.entity.Product;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE p.order.uuid = :ordersUuid")
    List<Product> findByOrdersUuid(@Param("ordersUuid") UUID ordersUuid);
}
