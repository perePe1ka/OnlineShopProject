package ru.vladuss.grpsclient.config;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;
import ru.vladuss.grpsbackend.OrderCostControllerServiceGrpc;
import ru.vladuss.grpsbackend.OrderRequest;
import ru.vladuss.grpsbackend.OrderResponse;

@Component
public class GrpsClient {

    private final ManagedChannel managedChannel;
    private final OrderCostControllerServiceGrpc.OrderCostControllerServiceBlockingStub stub;

    public GrpsClient() {
        this.managedChannel = ManagedChannelBuilder.forAddress("localhost", 9090)
                .usePlaintext()
                .build();
        this.stub = OrderCostControllerServiceGrpc.newBlockingStub(managedChannel);
    }

    public OrderResponse validateOrderCost(OrderRequest request) {
        try {
            return stub.validateOrderCost(request);
        } catch (Exception e) {
            System.err.println("Ошибка вызова gRPC метода: " + e.getMessage());
            throw e;
        }
    }

    public void close() {
        managedChannel.shutdown();
    }
}
