package com.blackcode.orders.repository;

import com.blackcode.orders.model.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

    @Query(value = "SELECT o.* FROM tb_orders o WHERE o.users_id = :userId", nativeQuery = true)
    Page<Orders> findOrderByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "SELECT o.* FROM tb_orders o WHERE o.users_id = :userId AND o.id = :orderId", nativeQuery = true)
    Optional<Orders> findOrderByUserIdAndOrderId(@Param("userId") Long userId, @Param("orderId") Long orderId);
}