package com.rishabh.librarymanagement.pojo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserPojo implements Serializable {

    @NotNull
    String loginId;

    @NotNull
    String libraryCode;

    @NotNull
    String role;

    @NotNull
    String name;

}
