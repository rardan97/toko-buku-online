package com.blackcode.service;


import com.blackcode.books.service.BooksService;
import com.blackcode.common.dto.reports.BestSellerRes;
import com.blackcode.common.dto.reports.PriceRes;
import com.blackcode.common.dto.reports.SalesRes;
import com.blackcode.orders.service.OrdersService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportsServiceImpl implements ReportsService{

    private final OrdersService ordersService;

    private final BooksService booksService;

    public ReportsServiceImpl(OrdersService ordersService, BooksService booksService) {
        this.ordersService = ordersService;
        this.booksService = booksService;
    }

    @Override
    public SalesRes getSales() {
        return ordersService.getSalesReport();
    }

    @Override
    public List<BestSellerRes> getBestseller() {
        return ordersService.getBestSeller();
    }

    @Override
    public PriceRes getPrice() {
        return booksService.getBookPrice();
    }
}
