package com.rishabh.librarymanagement.repository;

import com.rishabh.librarymanagement.dao.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
