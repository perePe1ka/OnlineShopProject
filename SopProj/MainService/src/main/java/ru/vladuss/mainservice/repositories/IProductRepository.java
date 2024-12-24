package ru.vladuss.mainservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vladuss.mainservice.entity.Product;

import java.util.UUID;

@Repository
public interface IProductRepository extends JpaRepository<Product, UUID> {
}
