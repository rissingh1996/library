package com.rishabh.librarymanagement.repository;

import com.rishabh.librarymanagement.dao.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "Select b from Book b where b.bookName like CONCAT('%',:keyword,'%') or b.category like CONCAT('%',:keyword,'%') or b.author like CONCAT('%',:keyword,'%')")
    List<Book> findAllByBookNameOrAuthorOrCategory(@Param("keyword") String keyword, Pageable pageable);

}
