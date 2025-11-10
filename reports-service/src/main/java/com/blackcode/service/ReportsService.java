package com.blackcode.service;

import com.blackcode.common.dto.reports.BestSellerRes;
import com.blackcode.common.dto.reports.PriceRes;
import com.blackcode.common.dto.reports.SalesRes;

import java.util.List;

public interface ReportsService {

    SalesRes getSales();

    List<BestSellerRes> getBestseller();

    PriceRes getPrice();
}
