package ru.vladuss.grpsbackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import ru.vladuss.grpsbackend.service.OrderValidationService;

@Configuration
public class GrpcServerConfig {

    private OrderValidationService orderValidationService;

    @Autowired
    public GrpcServerConfig(OrderValidationService orderValidationService) {
        this.orderValidationService = orderValidationService;
    }

    @Bean
    public Server grpcServer() throws Exception {
        return ServerBuilder.forPort(9090)
                .addService(orderValidationService)
                .build();
    }
}
