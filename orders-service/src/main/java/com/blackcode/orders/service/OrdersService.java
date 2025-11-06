package com.blackcode.orders.service;

import com.blackcode.common.dto.orders.OrdersReq;
import com.blackcode.common.dto.orders.OrdersRes;
import com.blackcode.common.dto.orders.PayReq;
import com.blackcode.common.dto.orders.PayRes;
import org.springframework.data.domain.Page;

public interface OrdersService {

    OrdersRes createOrders(OrdersReq ordersReq);

    PayRes payOrder(Long orderId, PayReq payReq);

    Page<OrdersRes> getAllOrders(int page, int size);

    OrdersRes getDetailOrdersById(Long orderId);

}
