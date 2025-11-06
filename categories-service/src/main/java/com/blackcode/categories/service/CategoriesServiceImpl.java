package com.blackcode.categories.service;

import com.blackcode.categories.model.Categories;
import com.blackcode.categories.repository.CategoriesRepository;
import com.blackcode.common.dto.categories.CategoriesReq;
import com.blackcode.common.dto.categories.CategoriesRes;
import com.blackcode.common.exception.DataNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CategoriesServiceImpl implements CategoriesService{

    private final CategoriesRepository categoriesRepository;

    public CategoriesServiceImpl(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }


    @Override
    public Page<CategoriesRes> getAllCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Categories> categoriesList = categoriesRepository.findAll(pageable);
        return categoriesList.map(this::mapToCategoriesRes);
    }

    @Override
    public CategoriesRes getCategoriesById(Long categoriesId) {
        Categories categories = categoriesRepository.findById(categoriesId)
                .orElseThrow(() -> new DataNotFoundException("Categories not found with id: "+categoriesId));
        return mapToCategoriesRes(categories);
    }

    @Override
    @Transactional
    public CategoriesRes createCategories(CategoriesReq categoriesReq) {
        Categories categories = new Categories();
        categories.setName(categoriesReq.getName());
        Categories savedCategories = categoriesRepository.save(categories);
        return mapToCategoriesRes(savedCategories);

    }

    @Override
    @Transactional
    public CategoriesRes updateCategories(Long categoriesId, CategoriesReq categoriesReq) {
        Categories categories = categoriesRepository.findById(categoriesId)
                .orElseThrow(() -> new DataNotFoundException("Categories not found with id: "+categoriesId));

        categories.setName(categoriesReq.getName());
        Categories updatedCategories = categoriesRepository.save(categories);
        return mapToCategoriesRes(updatedCategories);
    }

    @Override
    @Transactional
    public Map<String, Object> deleteCategories(Long categoriesId) {
        Categories categories = categoriesRepository.findById(categoriesId)
                .orElseThrow(() -> new DataNotFoundException("Categories not found with id: "+categoriesId));
        categoriesRepository.delete(categories);
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("categoriesId", categoriesId);
        responseData.put("info", "The Categories was removed from the database.");
        return responseData;
    }

    private CategoriesRes mapToCategoriesRes(Categories categories) {
        CategoriesRes categoriesRes = new CategoriesRes();
        categoriesRes.setId(categories.getId());
        categoriesRes.setName(categories.getName());
        return categoriesRes;
    }

}
