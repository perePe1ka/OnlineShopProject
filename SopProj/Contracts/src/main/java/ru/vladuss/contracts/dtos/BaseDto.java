package ru.vladuss.contracts.dtos;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseDto {
    private String uuid;

    public BaseDto() {
    }

    public BaseDto(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}

