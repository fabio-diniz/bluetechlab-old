package com.ciandt.techlab.order.entrypoints.controllers;

import com.ciandt.techlab.order.entities.Order;
import com.ciandt.techlab.order.entrypoints.controllers.dto.CreateOrderDTO;
import com.ciandt.techlab.order.entrypoints.controllers.dto.UpdateOrderDTO;
import com.ciandt.techlab.order.usecases.CreateOrder;
import com.ciandt.techlab.order.usecases.DeleteOrder;
import com.ciandt.techlab.order.usecases.GetOrder;
import com.ciandt.techlab.order.usecases.UpdateOrder;
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
    private UpdateOrder updateOrder;
    private DeleteOrder deleteOrder;

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody final CreateOrderDTO orderDTO) {
        final String orderId = createOrder.execute(
                Order.builder()
                        .operator(orderDTO.getOperator())
                        .currentAmount(orderDTO.getAmount())
                        .description(orderDTO.getDescription())
                        .discount(orderDTO.getDiscount() == null ? BigDecimal.ZERO : orderDTO.getDiscount())
                        .build()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(orderId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> update(@PathVariable final String id, @Valid @RequestBody final UpdateOrderDTO orderDTO) {
        final Order updatedOder = updateOrder.execute(id,
                Order.builder()
                        .operator(orderDTO.getOperator())
                        .description(orderDTO.getDescription())
                        .discount(orderDTO.getDiscount() == null ? BigDecimal.ZERO : orderDTO.getDiscount())
                        .build()
        );

        if (updatedOder == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(updatedOder);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final String id) {
        deleteOrder.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable final String id) {
        final Order order = getOrder.execute(id);

        if (order == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(order);
        }
    }
}
