package com.rishabh.librarymanagement.pojo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookIssueDto {

    String userId;

    String bookNo;

}
