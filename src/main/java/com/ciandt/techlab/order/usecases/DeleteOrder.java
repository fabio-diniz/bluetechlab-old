package com.ciandt.techlab.order.usecases;

import com.ciandt.techlab.order.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteOrder {

    private final OrderRepository orderRepository;

    public void execute(final String id) {
        orderRepository.delete(id);
    }
}
