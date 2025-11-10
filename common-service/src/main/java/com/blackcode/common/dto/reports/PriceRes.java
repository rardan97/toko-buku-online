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
public class PriceRes {

    private BigDecimal maxPrice;

    private BigDecimal minPrice;

    private BigDecimal avgPrice;

}
