package com.blackcode.common.dto.orders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PayRes {

    private Long orderId;

    private Long userId;

    private BigDecimal amount;

    private String status;

    private String message;

}
