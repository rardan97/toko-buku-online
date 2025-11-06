package com.blackcode.books.service;

import com.blackcode.common.dto.book.BooksReq;
import com.blackcode.common.dto.book.BooksRes;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface BooksService {

    Page<BooksRes> getAllBooks(int page, int size);

    BooksRes getBooksById(Long booksId);

    BooksRes createBooks(BooksReq booksReq);

    BooksRes updateBooks(Long booksId, BooksReq booksReq);

    BooksRes updateBooksStock(Long booksId, int stock);

    Map<String, Object> deleteBooks(Long booksId);

}
