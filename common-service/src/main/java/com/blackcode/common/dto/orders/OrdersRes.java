package com.blackcode.common.dto.orders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrdersRes {

    private Long id;

    private BigDecimal total_price;

    private String status;

    private List<OrderItemsRes> items;

    private Timestamp created_at;

}
