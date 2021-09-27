package com.rishabh.librarymanagement.pojo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookAddDto {

    String category;

    String bookNo;

    String bookName;

    String author;

    String libraryCode;

    Integer bookCount = 0;

}
