package com.rishabh.librarymanagement.pojo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserPojo implements Serializable {

    String loginId;

    String libraryCode;

    String role;

    String name;

}
