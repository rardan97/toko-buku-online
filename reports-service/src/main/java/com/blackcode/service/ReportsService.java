package com.blackcode.service;

import com.blackcode.common.dto.reports.BestSellerRes;
import com.blackcode.common.dto.reports.PriceRes;
import com.blackcode.common.dto.reports.SalesRes;

public interface ReportsService {

    SalesRes getSales();

    BestSellerRes getBestseller();

    PriceRes getPrice();
}
