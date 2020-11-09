package com.ciandt.techlab.order.entrypoints.controllers.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder(toBuilder = true)
public class ResponseOrderDTO {

    private String id;
    private String salesman;
    private String description;
    private BigDecimal amount;
    private BigDecimal discount;
}
