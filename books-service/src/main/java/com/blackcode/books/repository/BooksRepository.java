package com.blackcode.books.repository;

import com.blackcode.books.model.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BooksRepository extends JpaRepository<Books, Long> {

    @Query(value = "SELECT MAX(price) AS maxPrice, MIN(price) AS minPrice, AVG(price) AS avgPrice " +
            "FROM tb_books", nativeQuery = true)
    Object[] getBookPrice();

}
