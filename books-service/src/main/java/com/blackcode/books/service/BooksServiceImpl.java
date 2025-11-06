package com.blackcode.books.service;

import com.blackcode.books.model.Books;
import com.blackcode.books.repository.BooksRepository;
import com.blackcode.books.utils.FileUtils;
import com.blackcode.categories.model.Categories;
import com.blackcode.categories.service.CategoriesService;
import com.blackcode.common.dto.book.BooksReq;
import com.blackcode.common.dto.book.BooksRes;
import com.blackcode.common.dto.categories.CategoriesRes;
import com.blackcode.common.exception.DataNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class BooksServiceImpl implements BooksService{

    private final BooksRepository booksRepository;

    private final CategoriesService categoriesService;

    public BooksServiceImpl(BooksRepository booksRepository, CategoriesService categoriesService) {
        this.booksRepository = booksRepository;
        this.categoriesService = categoriesService;
    }

    @Override
    public Page<BooksRes> getAllBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return booksRepository.findAll(pageable).map(this::mapToBooksRes);
    }

    @Override
    public BooksRes getBooksById(Long booksId) {
        Books books = booksRepository.findById(booksId)
                .orElseThrow(() -> new DataNotFoundException("Books not found with id: " + booksId));
        return mapToBooksRes(books);
    }

    @Override
    public BooksRes createBooks(BooksReq booksReq) {
        Books books = mapReqToBooks(booksReq, new Books());
        Books savedBooks = booksRepository.save(books);
        return mapToBooksRes(savedBooks);
    }

    @Override
    public BooksRes updateBooks(Long booksId, BooksReq booksReq) {
        Books books = booksRepository.findById(booksId)
                .orElseThrow(() -> new DataNotFoundException("Books not found with id: " + booksId));

        Books booksMap = mapReqToBooks(booksReq, books);
        Books savedBooks = booksRepository.save(booksMap);
        return mapToBooksRes(savedBooks);
    }

    @Override
    public BooksRes updateBooksStock(Long booksId, int stock) {
        Books books = booksRepository.findById(booksId)
                .orElseThrow(() -> new DataNotFoundException("Books not found with id: " + booksId));
        books.setStock(stock);
        Books savedBooks = booksRepository.save(books);
        return mapToBooksRes(savedBooks);
    }

    @Override
    public Map<String, Object> deleteBooks(Long booksId) {
        Books books = booksRepository.findById(booksId)
                .orElseThrow(() -> new DataNotFoundException("Books not found with id: " + booksId));
        booksRepository.delete(books);

        Map<String, Object> response = new HashMap<>();
        response.put("booksId", booksId);
        response.put("info", "The book was removed from the database.");
        return response;
    }


    private Books mapReqToBooks(BooksReq booksReq, Books books) {
        CategoriesRes categoriesRes = categoriesService.getCategoriesById(Long.valueOf(booksReq.getCategory()));
        books.setTitle(booksReq.getTitle());
        books.setAuthor(booksReq.getAuthor());
        books.setPrice(booksReq.getPrice());
        books.setStock(booksReq.getStock());
        books.setYear(booksReq.getYear());
        books.setCategory(mapToCategories(categoriesRes));

        if (booksReq.getImage_base64() != null && !booksReq.getImage_base64().isEmpty()) {
            try {
                String imagePath = saveImageFromBase64(booksReq.getImage_base64());
                books.setImagePath(imagePath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save book image", e);
            }
        }
        return books;
    }

    private String saveImageFromBase64(String base64) throws IOException {
        String cleanedBase64 = base64.replaceAll("^data:image/[^;]+;base64,", "")
                .replaceAll("\\s", "");
        return FileUtils.saveBase64ImageToFile(cleanedBase64);
    }

    private BooksRes mapToBooksRes(Books books) {
        BooksRes res = new BooksRes();
        res.setId(books.getId());
        res.setTitle(books.getTitle());
        res.setAuthor(books.getAuthor());
        res.setPrice(books.getPrice());
        res.setStock(books.getStock());
        res.setYear(books.getYear());
        res.setCategory(mapToCategoriesRes(books.getCategory()));

        res.setImagePath(books.getImagePath());
        return res;
    }

    private Categories mapToCategories(CategoriesRes res) {
        Categories categories = new Categories();
        categories.setId(res.getId());
        categories.setName(res.getName());
        return categories;
    }

    private CategoriesRes mapToCategoriesRes(Categories categories) {
        CategoriesRes res = new CategoriesRes();
        res.setId(categories.getId());
        res.setName(categories.getName());
        return res;
    }

}
