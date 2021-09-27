package com.rishabh.librarymanagement.repository;

import com.rishabh.librarymanagement.dao.Book;
import com.rishabh.librarymanagement.dao.BookInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookInventoryRepository extends JpaRepository<BookInventory, Long> {
    BookInventory findByBookAndLibraryCode(Book book, String libraryCode);

    List<BookInventory> findByBook(Book book);

}
