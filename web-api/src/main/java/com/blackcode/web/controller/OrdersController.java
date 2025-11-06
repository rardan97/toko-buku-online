package com.blackcode.web.controller;

import com.blackcode.common.dto.orders.OrdersReq;
import com.blackcode.common.dto.orders.OrdersRes;
import com.blackcode.common.dto.orders.PayReq;
import com.blackcode.common.dto.orders.PayRes;
import com.blackcode.common.utils.ApiResponse;
import com.blackcode.orders.service.OrdersService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {

    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrdersRes>> createOrders(@RequestBody OrdersReq ordersReq){
        OrdersRes ordersRes = ordersService.createOrders(ordersReq);
        return ResponseEntity.ok(ApiResponse.success("Order created", 201, ordersRes));
    }

    @PostMapping("/{orderId}/pay")
    public ResponseEntity<ApiResponse<PayRes>> payOrder(@PathVariable("orderId") Long orderId, @RequestBody PayReq payReq){
        PayRes payRes = ordersService.payOrder(orderId, payReq);
        return ResponseEntity.ok(ApiResponse.success("Pay", 201, payRes));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<OrdersRes>>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        Page<OrdersRes> ordersRes = ordersService.getAllOrders(page, size);
        return ResponseEntity.ok(ApiResponse.success("Categories retrieved successfully", 200, ordersRes));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrdersRes>> getDetailOrdersById(@PathVariable("orderId") Long orderId){
        OrdersRes ordersRes = ordersService.getDetailOrdersById(orderId);
        return ResponseEntity.ok(ApiResponse.success("Categories retrieved successfully", 200, ordersRes));
    }
}
