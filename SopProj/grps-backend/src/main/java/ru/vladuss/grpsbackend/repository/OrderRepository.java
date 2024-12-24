package ru.vladuss.grpsbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.vladuss.grpsbackend.entity.Orders;

import java.util.UUID;


public interface OrderRepository extends JpaRepository<Orders, Long> {
    @Query("SELECT o FROM Orders o WHERE o.uuid = :uuid")
    Orders findByUuid(@Param("uuid") UUID uuid);

}
