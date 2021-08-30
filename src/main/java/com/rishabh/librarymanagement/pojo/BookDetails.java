package com.rishabh.librarymanagement.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.rishabh.librarymanagement.dao.Book;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class BookDetails implements Serializable {

    @JsonUnwrapped
    Book book;

    Boolean isAvailable;

    Date returnDate;

}
