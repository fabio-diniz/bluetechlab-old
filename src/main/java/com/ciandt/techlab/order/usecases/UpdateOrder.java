package com.ciandt.techlab.order.usecases;

import com.ciandt.techlab.order.entities.Order;
import com.ciandt.techlab.order.repositories.OrderRepository;
import com.ciandt.techlab.order.repositories.dto.OrderData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class UpdateOrder {

    private final GetOrder getOrder;
    private final OrderRepository orderRepository;

    public Order execute(final String id, final Order order) {
        final Order obtainedOrder = getOrder.execute(id);

        if (obtainedOrder != null) {
            return orderRepository.update(
                    OrderData.builder()
                            .id(obtainedOrder.getId())
                            .currentAmount(applyDiscount(obtainedOrder.getOriginalAmount(), order))
                            .discount(order.getDiscount())
                            .description(order.getDescription())
                            .responsibleUser(order.getOperator())
                            .build()
            ).toEntity();
        } else {
            return null;
        }
    }

    private BigDecimal applyDiscount(final BigDecimal obtainedAmount, final Order order) {
        return obtainedAmount.subtract(order.getDiscount());
    }
}
