package com.ciandt.techlab.order.usecases;

import com.ciandt.techlab.order.configurations.constansts.ErrorsConstants;
import com.ciandt.techlab.order.entities.Order;
import com.ciandt.techlab.order.repositories.OrderRepository;
import com.ciandt.techlab.order.repositories.dto.OrderData;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class CreateOrder {

    private final OrderRepository orderRepository;

    public Order execute(final Order order) {
        return orderRepository.save(
                OrderData.builder()
                        .currentAmount(applyDiscount(order))
                        .discount(order.getDiscount())
                        .originalAmount(order.getCurrentAmount())
                        .description(order.getDescription())
                        .salesman(order.getSalesman())
                        .build()
        ).toEntity();
    }

    private BigDecimal applyDiscount(final Order order) {
        if (order.getCurrentAmount().compareTo(order.getDiscount()) > 0) {
            return order.getCurrentAmount().subtract(order.getDiscount());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ErrorsConstants.ORDER_INVALID_DISCOUNT, new IllegalArgumentException());
        }
    }
}
