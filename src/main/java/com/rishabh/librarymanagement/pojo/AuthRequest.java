package com.rishabh.librarymanagement.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthRequest implements Serializable {

    String loginId;
    String libraryCode;
}
