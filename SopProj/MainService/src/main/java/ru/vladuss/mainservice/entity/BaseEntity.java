package ru.vladuss.mainservice.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.util.UUID;


@MappedSuperclass
public abstract class BaseEntity {
    protected UUID uuid;

    protected BaseEntity() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
