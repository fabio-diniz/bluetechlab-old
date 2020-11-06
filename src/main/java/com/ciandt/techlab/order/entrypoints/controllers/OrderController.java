package com.ciandt.techlab.order.entrypoints.controllers;

import com.ciandt.techlab.order.entities.Order;
import com.ciandt.techlab.order.entrypoints.controllers.dto.OrderDTO;
import com.ciandt.techlab.order.usecases.CreateOrder;
import com.ciandt.techlab.order.usecases.GetOrder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@AllArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private GetOrder getOrder;
    private CreateOrder createOrder;

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody final OrderDTO orderDTO) {
        final String orderId = createOrder.execute(
                Order.builder()
                        .amount(orderDTO.getAmount())
                        .operator(orderDTO.getOperator())
                        .description(orderDTO.getDescription())
                        .discount(orderDTO.getDiscount() == null ? BigDecimal.ZERO : orderDTO.getDiscount())
                        .build()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(orderId);
    }

    @PutMapping
    public ResponseEntity update(final OrderDTO orderDTO) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete() {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity getOrder(@PathVariable final String id) {
        return ResponseEntity.status(HttpStatus.OK).body(getOrder.execute(id));
    }
}
