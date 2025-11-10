package com.blackcode.common.dto.reports;

import com.blackcode.common.dto.book.BooksRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PriceRes {

    private BigDecimal maxPrice;

    private BigDecimal minPrice;

    private BigDecimal avgPrice;

}
