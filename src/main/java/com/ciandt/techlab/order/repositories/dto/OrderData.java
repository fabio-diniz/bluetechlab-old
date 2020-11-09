package com.ciandt.techlab.order.repositories.dto;

import com.ciandt.techlab.order.entities.Order;
import com.ciandt.techlab.order.repositories.ObjectData;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder(toBuilder = true)
public class OrderData extends ObjectData<Order> {

    private String id;
    private String salesman;
    private String description;
    private BigDecimal discount;
    private BigDecimal currentAmount;
    private BigDecimal originalAmount;

    @Override
    public Order toEntity() {

        return Order.builder()
                .id(this.id)
                .discount(this.discount)
                .description(this.description)
                .salesman(this.salesman)
                .currentAmount(this.currentAmount)
                .originalAmount(this.originalAmount)
                .build();
    }
}
