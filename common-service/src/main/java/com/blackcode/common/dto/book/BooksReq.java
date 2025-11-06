package com.blackcode.common.dto.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BooksReq {

    private String title;

    private String author;

    private BigDecimal price;

    private int stock;

    private int year;

    private String category;

    private String image_base64;

}
