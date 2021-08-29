package com.rishabh.librarymanagement.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.rishabh.librarymanagement.dao.Book;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BookDto implements Serializable {

    @JsonUnwrapped
    Book book;

    Date dueDate;

    Date issuedDate;

    Date returnedDate;

}
