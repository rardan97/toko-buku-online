package com.blackcode.common.dto.orders;

import com.blackcode.common.dto.book.BooksRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItemsRes {

    private Long id;

    private BooksRes books;

    private int quantity;

    private BigDecimal price;

}
