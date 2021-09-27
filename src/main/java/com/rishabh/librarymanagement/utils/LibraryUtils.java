package com.rishabh.librarymanagement.utils;

import com.rishabh.librarymanagement.dao.Book;
import com.rishabh.librarymanagement.dao.BookInventory;
import com.rishabh.librarymanagement.dao.Library;
import com.rishabh.librarymanagement.dao.User;
import com.rishabh.librarymanagement.pojo.BookAddDto;
import com.rishabh.librarymanagement.pojo.UserPojo;
import org.springframework.stereotype.Component;

@Component
public class LibraryUtils {
    public static User getUserDao(UserPojo userPojo, Library library) {
        User user = new User();
        user.setLibrary(library);
        user.setLoginId(userPojo.getLoginId());
        user.setName(userPojo.getName());
        user.setRole(userPojo.getRole());
        return user;
    }

    public static BookInventory getBookInventory(BookAddDto bookDetails, Book book) {
        BookInventory bookInventory = new BookInventory();
        bookInventory.setBook(book);
        bookInventory.setBookCount(bookDetails.getBookCount());
        bookInventory.setLibraryCode(bookDetails.getLibraryCode());
        return bookInventory;
    }

    public static Book getBook(BookAddDto bookDetails) {
        Book book = new Book();
        book.setBookNo(bookDetails.getBookNo());
        book.setBookName(bookDetails.getBookName());
        book.setAuthor(bookDetails.getAuthor());
        book.setCategory(bookDetails.getCategory());
        return book;
    }
}
