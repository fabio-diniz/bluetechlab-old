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
    private String description;
    private String responsibleUser;
    private BigDecimal amount;
    private BigDecimal discount;
    private BigDecimal originalAmount;

    @Override
    public Order toEntity() {

        return Order.builder()
                .id(this.id)
                .amount(this.amount)
                .discount(this.discount)
                .description(this.description)
                .operator(this.responsibleUser)
                .build();
    }
}
