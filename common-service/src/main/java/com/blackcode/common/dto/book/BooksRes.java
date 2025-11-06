package com.blackcode.common.dto.book;

import com.blackcode.common.dto.categories.CategoriesRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BooksRes {

    private Long id;

    private String title;

    private String author;

    private BigDecimal price;

    private int stock;

    private int year;

    private CategoriesRes category;

    private String imagePath;

}
