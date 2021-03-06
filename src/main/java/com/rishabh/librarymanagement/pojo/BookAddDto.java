package com.rishabh.librarymanagement.pojo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookAddDto {

    String category;

    @NotNull
    String bookNo;

    @NotNull
    String bookName;

    String author;

    @NotNull
    String libraryCode;

    @NotNull
    Integer bookCount = 0;

}
