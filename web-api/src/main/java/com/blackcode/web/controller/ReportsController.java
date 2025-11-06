package com.blackcode.web.controller;

import com.blackcode.common.dto.reports.BestSellerRes;
import com.blackcode.common.dto.reports.PriceRes;
import com.blackcode.common.dto.reports.SalesRes;
import com.blackcode.common.utils.ApiResponse;
import com.blackcode.service.ReportsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportsController {

    private final ReportsService reportsService;

    public ReportsController(ReportsService reportsService) {
        this.reportsService = reportsService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/sales")
    public ResponseEntity<ApiResponse<SalesRes>> getReportSales(){
        SalesRes salesRes = reportsService.getSales();
        return ResponseEntity.ok(ApiResponse.success("Report Sales",200, salesRes));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/bestseller")
    public ResponseEntity<ApiResponse<BestSellerRes>> getReportBestseller(){
        BestSellerRes bestSellerRes = reportsService.getBestseller();
        return ResponseEntity.ok(ApiResponse.success("Report Bestseller",200, bestSellerRes));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/prices")
    public ResponseEntity<ApiResponse<PriceRes>> getReportPrices(){
        PriceRes priceRes = reportsService.getPrice();
        return ResponseEntity.ok(ApiResponse.success("Report Prices",200, priceRes));
    }
}
