package com.blackcode.books.model;

import com.blackcode.categories.model.Categories;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_books")
public class Books {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    @NotBlank
    private String title;

    @Column(nullable = false, length = 100)
    @NotBlank
    private String author;

    @PositiveOrZero
    private BigDecimal price;

    @PositiveOrZero
    private int stock;

    private int year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Categories category;

    @Column(name = "image_path")
    private String imagePath;

}
