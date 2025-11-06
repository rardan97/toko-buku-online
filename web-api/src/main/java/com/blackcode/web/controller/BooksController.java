package com.blackcode.web.controller;

import com.blackcode.books.service.BooksService;
import com.blackcode.common.dto.book.BooksReq;
import com.blackcode.common.dto.book.BooksRes;
import com.blackcode.common.utils.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BooksController {

    private final BooksService booksService;

    public BooksController(BooksService booksService) {
        this.booksService = booksService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BooksRes>>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        Page<BooksRes> booksResList = booksService.getAllBooks(page, size);
        return ResponseEntity.ok(ApiResponse.success("Books retrieved successfully", 200, booksResList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BooksRes>> getBooksById(@PathVariable("id") Long id){
        BooksRes categoryRes = booksService.getBooksById(id);
        return ResponseEntity.ok(ApiResponse.success("Books found",200, categoryRes));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<BooksRes>> createBooks(@RequestBody BooksReq booksReq){
        BooksRes categoryRes = booksService.createBooks(booksReq);
        return ResponseEntity.ok(ApiResponse.success("Books created", 201, categoryRes));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BooksRes>> updateBooks(@PathVariable("id") Long id, @RequestBody BooksReq booksReq){
        BooksRes categoryRes = booksService.updateBooks(id, booksReq);
        return ResponseEntity.ok(ApiResponse.success("Books Update", 200, categoryRes));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> deleteBooks(@PathVariable("id") Long id){
        Map<String, Object> rtn = booksService.deleteBooks(id);
        return ResponseEntity.ok(ApiResponse.success("Books deleted successfully", 200, rtn));
    }


}
