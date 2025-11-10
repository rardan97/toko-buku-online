package com.blackcode.orders.service;

import com.blackcode.books.model.Books;
import com.blackcode.books.service.BooksService;
import com.blackcode.categories.model.Categories;
import com.blackcode.common.dto.book.BooksRes;
import com.blackcode.common.dto.categories.CategoriesRes;
import com.blackcode.common.dto.orders.*;
import com.blackcode.common.dto.reports.BestSellerRes;
import com.blackcode.common.dto.reports.SalesRes;
import com.blackcode.common.dto.user.UserRes;
import com.blackcode.common.exception.DataNotFoundException;
import com.blackcode.common.exception.InvalidPaymentException;
import com.blackcode.common.exception.UnauthorizedException;
import com.blackcode.orders.model.OrderItems;
import com.blackcode.orders.model.Orders;
import com.blackcode.orders.model.Status;
import com.blackcode.orders.repository.OrderItemsRepository;
import com.blackcode.orders.repository.OrdersRepository;
import com.blackcode.user.model.Role;
import com.blackcode.user.model.Users;
import com.blackcode.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl implements OrdersService {

    private final UserService userService;

    private final BooksService booksService;

    private final OrdersRepository ordersRepository;

    private final OrderItemsRepository orderItemsRepository;

    public OrdersServiceImpl(UserService userService, BooksService booksService, OrdersRepository ordersRepository, OrderItemsRepository orderItemsRepository) {
        this.userService = userService;
        this.booksService = booksService;
        this.ordersRepository = ordersRepository;
        this.orderItemsRepository = orderItemsRepository;
    }

    @Override
    public OrdersRes createOrders(OrdersReq ordersReq) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Orders order = new Orders();
        order.setUser(mapToUsers(userService.findByEmail(email)));
        order.setStatus(Status.PENDING);

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderItemsReq itemReq : ordersReq.getOrderItems()) {
            BooksRes books = booksService.getBooksById(itemReq.getBookId());

            if (books.getStock() < itemReq.getQuantity()) {
                throw new IllegalArgumentException(
                        "Stok buku " + books.getTitle() + " tidak cukup. Tersedia: " + books.getStock()
                );
            }

            OrderItems orderItem = new OrderItems();
            orderItem.setBooks(mapToBooks(books));
            orderItem.setQuantity(itemReq.getQuantity());
            BigDecimal itemTotal = books.getPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
            orderItem.setPrice(itemTotal);
            totalPrice = totalPrice.add(itemTotal);
            orderItem.setOrders(order);
            order.getItems().add(orderItem);
            booksService.updateBooksStock(books.getId(), books.getStock() - itemReq.getQuantity());
        }

        order.setTotal_price(totalPrice);
        order.setCreated_at(new Timestamp(System.currentTimeMillis()));
        Orders savedOrder = ordersRepository.save(order);

        return mapToOrdersRes(savedOrder);
    }

    @Override
    public PayRes payOrder(Long orderId, PayReq payReq) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserRes userRes = userService.findByEmail(email);

        Orders orders = ordersRepository.findById(orderId)
                .orElseThrow(() -> new DataNotFoundException("Order not found with ID: " + orderId));

        if (!orders.getUser().getId().equals(userRes.getId())) {
            throw new UnauthorizedException("User is not authorized to pay for this order");
        }

        BigDecimal orderTotal = orders.getTotal_price();
        BigDecimal paymentAmount = payReq.getAmount();

        if (paymentAmount.compareTo(orderTotal) != 0) {
            throw new InvalidPaymentException("Payment amount must match the order total exactly");
        }

        orders.setStatus(Status.PAID);
        ordersRepository.save(orders);

        PayRes payRes = new PayRes();
        payRes.setOrderId(orders.getId());
        payRes.setUserId(userRes.getId());
        payRes.setAmount(paymentAmount);
        payRes.setStatus(orders.getStatus().name());
        payRes.setMessage("Payment successful");
        return payRes;
    }

    @Override
    public Page<OrdersRes> getAllOrders(int page, int size) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserRes userRes = userService.findByEmail(email);
        Pageable pageable = PageRequest.of(page, size);
        Page<Orders> orders;

        if ("ADMIN".equals(userRes.getRole())) {
            orders = ordersRepository.findAll(pageable);
        } else {
            orders = ordersRepository.findOrderByUserId(userRes.getId(), pageable);
        }

        return orders.map(this::mapToOrdersRes);
    }

    @Override
    public OrdersRes getDetailOrdersById(Long orderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UserRes userRes = userService.findByEmail(email);

        Orders orders = ordersRepository.findOrderByUserIdAndOrderId(userRes.getId(), orderId)
                .orElseThrow(() -> new DataNotFoundException("Order not found with ID: " + orderId));

        return mapToOrdersRes(orders);
    }

    @Override
    public SalesRes getSalesReport() {
        BigDecimal totalRevenue = orderItemsRepository.getTotalRevenue();
        Long totalBooksSold = orderItemsRepository.getTotalBooksSold();

        SalesRes salesRes = new SalesRes();
        salesRes.setTotal_revenue(totalRevenue != null ? totalRevenue : BigDecimal.ZERO);
        salesRes.setTotal_books_sold(totalBooksSold != null ? totalBooksSold : 0L);
        return salesRes;
    }

    @Override
    public List<BestSellerRes> getBestSeller() {
        return orderItemsRepository.findTop3BestSellingBooksNative()
                .stream()
                .map(row -> {
                    BestSellerRes b = new BestSellerRes();
                    b.setBookId(((Number) row[0]).longValue());
                    b.setTitle((String) row[1]);
                    b.setAuthor((String) row[2]);
                    b.setPrice(((Number) row[3]).doubleValue() != 0 ?
                            new java.math.BigDecimal(((Number) row[3]).doubleValue()) : null);
                    return b;
                })
                .collect(Collectors.toList());
    }


    private OrdersRes mapToOrdersRes(Orders orders) {
        OrdersRes ordersRes = new OrdersRes();
        ordersRes.setId(orders.getId());
        ordersRes.setTotal_price(orders.getTotal_price());
        ordersRes.setStatus(orders.getStatus().name());
        ordersRes.setItems(mapToOrderItemsRes(orders.getItems()));
        ordersRes.setCreated_at(orders.getCreated_at());
        return ordersRes;
    }

    private List<OrderItemsRes> mapToOrderItemsRes(List<OrderItems> orderItems) {
        List<OrderItemsRes> list = new ArrayList<>();
        for (OrderItems item : orderItems) {
            OrderItemsRes res = new OrderItemsRes();
            res.setId(item.getId());
            res.setBooks(mapToBooksRes(item.getBooks()));
            res.setQuantity(item.getQuantity());
            res.setPrice(item.getPrice());
            list.add(res);
        }
        return list;
    }

    private Users mapToUsers(UserRes userRes) {
        Users user = new Users();
        user.setId(userRes.getId());
        user.setName(userRes.getName());
        user.setRole(Role.valueOf(userRes.getRole()));
        user.setCreated_at(userRes.getCreated_at());
        return user;
    }

    private Books mapToBooks(BooksRes booksRes) {
        Books books = new Books();
        books.setId(booksRes.getId());
        books.setTitle(booksRes.getTitle());
        books.setAuthor(booksRes.getAuthor());
        books.setPrice(booksRes.getPrice());
        books.setStock(booksRes.getStock());
        books.setImagePath(booksRes.getImagePath());
        return books;
    }

    private BooksRes mapToBooksRes(Books books) {
        BooksRes dto = new BooksRes();
        dto.setId(books.getId());
        dto.setTitle(books.getTitle());
        dto.setAuthor(books.getAuthor());
        dto.setPrice(books.getPrice());
        dto.setStock(books.getStock());
        dto.setCategory(mapToCategoriesRes(books.getCategory()));
        dto.setImagePath(books.getImagePath());
        return dto;
    }

    private CategoriesRes mapToCategoriesRes(Categories categories) {
        CategoriesRes categoriesRes = new CategoriesRes();
        categoriesRes.setId(categories.getId());
        categoriesRes.setName(categories.getName());
        return categoriesRes;
    }

}
