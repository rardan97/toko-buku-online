package com.blackcode.orders.repository;

import com.blackcode.orders.model.OrderItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {

    @Query(value = "SELECT SUM(oi.price) " +
            "FROM tb_order_items oi " +
            "JOIN tb_orders o ON oi.order_id = o.id " +
            "WHERE o.status = 'PAID'", nativeQuery = true)
    BigDecimal getTotalRevenue();

    @Query(value = "SELECT SUM(oi.quantity) " +
            "FROM tb_order_items oi " +
            "JOIN tb_orders o ON oi.order_id = o.id " +
            "WHERE o.status = 'PAID'", nativeQuery = true)
    Long getTotalBooksSold();


    @Query(value = "SELECT b.id AS bookId, b.title, b.author, b.price, SUM(oi.quantity) AS totalSold " +
            "FROM tb_order_items oi " +
            "JOIN tb_orders o ON oi.order_id = o.id " +
            "JOIN tb_books b ON oi.books_id = b.id " +
            "WHERE o.status = 'PAID' " +
            "GROUP BY b.id, b.title, b.author, b.price " +
            "ORDER BY totalSold DESC " +
            "LIMIT 3", nativeQuery = true)
    List<Object[]> findTop3BestSellingBooksNative();


}
