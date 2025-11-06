package com.blackcode.categories.service;

import com.blackcode.common.dto.categories.CategoriesReq;
import com.blackcode.common.dto.categories.CategoriesRes;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface CategoriesService {

    Page<CategoriesRes> getAllCategories(int page, int size);

    CategoriesRes getCategoriesById(Long categoriesId);

    CategoriesRes createCategories(CategoriesReq categoriesReq);

    CategoriesRes updateCategories(Long categoriesId, CategoriesReq categoriesReq);

    Map<String, Object> deleteCategories(Long categoriesId);

}
