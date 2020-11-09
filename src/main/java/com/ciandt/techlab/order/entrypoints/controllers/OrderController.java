package com.ciandt.techlab.order.entrypoints.controllers;

import com.ciandt.techlab.order.configurations.constansts.ErrorsConstants;
import com.ciandt.techlab.order.entities.Order;
import com.ciandt.techlab.order.entrypoints.controllers.dto.RequestCreateOrderDTO;
import com.ciandt.techlab.order.entrypoints.controllers.dto.RequestUpdateOrderDTO;
import com.ciandt.techlab.order.entrypoints.controllers.dto.ResponseOrderDTO;
import com.ciandt.techlab.order.usecases.CreateOrder;
import com.ciandt.techlab.order.usecases.DeleteOrder;
import com.ciandt.techlab.order.usecases.GetOrder;
import com.ciandt.techlab.order.usecases.UpdateOrder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@AllArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final GetOrder getOrder;
    private final CreateOrder createOrder;
    private final UpdateOrder updateOrder;
    private final DeleteOrder deleteOrder;

    @PostMapping
    public ResponseEntity<ResponseOrderDTO> create(@Valid @RequestBody final RequestCreateOrderDTO orderDTO) {
        final Order order = Order.builder()
                .salesman(orderDTO.getSalesman())
                .currentAmount(orderDTO.getAmount())
                .description(orderDTO.getDescription())
                .discount(orderDTO.getDiscount() == null ? BigDecimal.ZERO : orderDTO.getDiscount())
                .build();

        final Order createdOrder = createOrder.execute(order);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseObject(createdOrder));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseOrderDTO> update(@PathVariable final String id, @Valid @RequestBody final RequestUpdateOrderDTO orderDTO) {
        final Order updatedOder = updateOrder.execute(id,
                Order.builder()
                        .salesman(orderDTO.getOperator())
                        .description(orderDTO.getDescription())
                        .discount(orderDTO.getDiscount() == null ? BigDecimal.ZERO : orderDTO.getDiscount())
                        .build()
        );

        checkNullableObject(updatedOder, HttpStatus.NOT_FOUND, ErrorsConstants.ORDER_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.OK).body(responseObject(updatedOder));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable final String id) {
        deleteOrder.execute(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseOrderDTO> getOrder(@PathVariable final String id) {
        final Order order = getOrder.execute(id);
        checkNullableObject(order, HttpStatus.NOT_FOUND, ErrorsConstants.ORDER_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.OK).body(responseObject(order));
    }

    public ResponseOrderDTO responseObject(final Order order) {
        return ResponseOrderDTO.builder()
                .id(order.getId())
                .salesman(order.getSalesman())
                .discount(order.getDiscount())
                .amount(order.getCurrentAmount())
                .description(order.getDescription())
                .build();
    }

    public void checkNullableObject(final Object object, final HttpStatus httpStatus, final String message) {
        if (object == null) {
            throw new ResponseStatusException(httpStatus, message);
        }
    }
}
