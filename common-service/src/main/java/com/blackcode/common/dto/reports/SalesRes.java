package com.blackcode.common.dto.reports;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SalesRes {

    private BigDecimal total_revenue;

    private Long total_books_sold;

}