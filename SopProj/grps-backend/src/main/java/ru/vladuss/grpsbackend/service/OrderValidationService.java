package ru.vladuss.grpsbackend.service;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vladuss.grpsbackend.OrderCostControllerServiceGrpc;
import ru.vladuss.grpsbackend.OrderRequest;
import ru.vladuss.grpsbackend.OrderResponse;
import ru.vladuss.grpsbackend.Status;

import java.util.UUID;

@Service
public class OrderValidationService extends OrderCostControllerServiceGrpc.OrderCostControllerServiceImplBase {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderValidationService.class);

    @Autowired
    private OrderService orderService;

    @Override
    public void validateOrderCost(OrderRequest request, StreamObserver<OrderResponse> responseObserver) {
        try {
            String orderId = request.getOrderId();
            if (orderId == null || orderId.isEmpty()) {
                throw new IllegalArgumentException("Идентификатор заказа отсутствует или пуст");
            }

            UUID orderUuid = UUID.fromString(request.getOrderId());
            double orderCost = request.getOrderCost();
            boolean discountWas = request.getIsDiscountWas();

            LOGGER.info("Обработка заказа с идентификатором: {}, ЦЕНА: {}, СКИДКА: {}", orderId, orderCost, discountWas);
            boolean isValid = orderService.validateOrder(orderUuid, orderCost, discountWas);

            String message;
            if (!isValid) {
                double correctedCost = orderService.calculateCorrectedCost(orderUuid, discountWas);
                LOGGER.info("Несоответствие стоимости заказа для идентификатора {}. ИЗМЕНЁН НА {}", orderId, correctedCost);
                orderService.updateOrderCost(orderUuid, correctedCost);
                message = "Несоответствие стоимости заказа. Обновлено до: " + correctedCost + " у заказа " + orderUuid;
            } else {
                message = "Заказ действителен";
            }

            OrderResponse response = OrderResponse.newBuilder()
                    .setOrderId(orderUuid.toString())
                    .setIsSuccess(isValid)
                    .setMessage(message)
                    .build();

            LOGGER.info("Проверка заказа для удостоверения личности завершена {}: {}", orderId, message);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            LOGGER.error("Неверный идентификатор заказа: {}", e.getMessage());
            responseObserver.onError(io.grpc.Status.INVALID_ARGUMENT
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        } catch (Exception e) {
            LOGGER.error("Непредвиденная ошибка при обработке заказа: ", e);
            responseObserver.onError(io.grpc.Status.INTERNAL
                    .withDescription("Внутренняя ошибка сервера: " + e.getMessage())
                    .withCause(e)
                    .asRuntimeException());
        }
    }


}
