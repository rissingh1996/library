package com.rishabh.librarymanagement.pojo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookIssueReturnDto {

    @NotNull
    String userId;

    @NotNull
    String bookNo;

}
