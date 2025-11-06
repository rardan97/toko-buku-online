package com.blackcode.web.controller;

import com.blackcode.categories.service.CategoriesService;
import com.blackcode.common.dto.categories.CategoriesReq;
import com.blackcode.common.dto.categories.CategoriesRes;
import com.blackcode.common.utils.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {

    private final CategoriesService categoriesService;

    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CategoriesRes>>> getAllCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        Page<CategoriesRes> categoryResList = categoriesService.getAllCategories(page, size);
        return ResponseEntity.ok(ApiResponse.success("Categories retrieved successfully", 200, categoryResList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoriesRes>> getCategoriesById(@PathVariable("id") Long id){
        CategoriesRes categoryRes = categoriesService.getCategoriesById(id);
        return ResponseEntity.ok(ApiResponse.success("Category found",200, categoryRes));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<CategoriesRes>> createCategories(@RequestBody CategoriesReq categoriesReq){
        CategoriesRes categoryRes = categoriesService.createCategories(categoriesReq);
        return ResponseEntity.ok(ApiResponse.success("Category created", 201, categoryRes));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoriesRes>> updateCategories(@PathVariable("id") Long id, @RequestBody CategoriesReq categoriesReq){
        CategoriesRes categoryRes = categoriesService.updateCategories(id, categoriesReq);
        return ResponseEntity.ok(ApiResponse.success("Category Update", 200, categoryRes));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> deleteCategories(@PathVariable("id") Long id){
        Map<String, Object> rtn = categoriesService.deleteCategories(id);
        return ResponseEntity.ok(ApiResponse.success("Category deleted successfully", 200, rtn));
    }
}
