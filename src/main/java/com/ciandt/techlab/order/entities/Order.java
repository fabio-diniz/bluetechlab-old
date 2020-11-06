package com.ciandt.techlab.order.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Order {

    private String id;
    private String operator;
    private String description;
    private BigDecimal amount;
    private BigDecimal discount;
}
