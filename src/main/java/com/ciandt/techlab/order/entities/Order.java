package com.ciandt.techlab.order.entities;

import lombok.*;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Builder(toBuilder = true)
public class Order {

    private String id;
    private String salesman;
    private String description;
    private BigDecimal discount;
    private BigDecimal currentAmount;
    private BigDecimal originalAmount;
}
