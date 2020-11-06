package com.ciandt.techlab.order.usecases;

import com.ciandt.techlab.order.entities.Order;
import com.ciandt.techlab.order.repositories.OrderRepository;
import com.ciandt.techlab.order.repositories.dto.OrderData;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@AllArgsConstructor
public class GetOrder {

    private final OrderRepository orderRepository;

    public Order execute(final String id) {
        final Optional<OrderData> optOrder = orderRepository.findById(id);
        return optOrder.map(OrderData::toEntity).orElse(null);
    }
}
