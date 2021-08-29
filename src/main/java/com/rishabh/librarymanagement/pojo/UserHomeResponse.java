package com.rishabh.librarymanagement.pojo;

import com.rishabh.librarymanagement.dao.Book;
import com.rishabh.librarymanagement.dao.Library;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserHomeResponse implements Serializable {

    List<BookDto> issuedBooks;

    Library library;

}
