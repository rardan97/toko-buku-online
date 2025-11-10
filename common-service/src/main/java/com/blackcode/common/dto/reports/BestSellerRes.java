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
public class BestSellerRes {

    private Long bookId;

    private String title;

    private String author;

    private BigDecimal price;

    private Long totalSold;
}
