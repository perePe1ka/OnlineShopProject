package ru.vladuss.mainservice.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(ru.vladuss.mainservice.constants.Status.class, ru.vladuss.contracts.dtos.Status.class)
                .setConverter(ctx -> ru.vladuss.contracts.dtos.Status.valueOf(ctx.getSource().name()));

        modelMapper.typeMap(ru.vladuss.contracts.dtos.Status.class, ru.vladuss.mainservice.constants.Status.class)
                .setConverter(ctx -> ru.vladuss.mainservice.constants.Status.valueOf(ctx.getSource().name()));

        return modelMapper;
    }


}
