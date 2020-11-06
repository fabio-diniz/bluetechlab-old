package com.ciandt.techlab.order.usecases;

import com.ciandt.techlab.order.entities.Order;
import com.ciandt.techlab.order.repositories.OrderRepository;
import com.ciandt.techlab.order.repositories.dto.OrderData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class CreateOrder {

    private final OrderRepository orderRepository;

    public String execute(final Order order) {
        return orderRepository.save(
                OrderData.builder()
                        .amount(applyDiscount(order))
                        .discount(order.getDiscount())
                        .originalAmount(order.getAmount())
                        .description(order.getDescription())
                        .responsibleUser(order.getOperator())
                        .build()
        ).getId();
    }

    private BigDecimal applyDiscount(final Order order) {
        return order.getAmount().subtract(order.getDiscount());
    }
}
