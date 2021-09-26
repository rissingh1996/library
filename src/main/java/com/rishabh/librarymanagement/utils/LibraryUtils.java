package com.rishabh.librarymanagement.utils;

import com.rishabh.librarymanagement.dao.Library;
import com.rishabh.librarymanagement.dao.User;
import com.rishabh.librarymanagement.pojo.UserPojo;
import org.springframework.stereotype.Component;

@Component
public class LibraryUtils {
    public User getUserDao(UserPojo userPojo, Library library) {
        User user = new User();
        user.setLibrary(library);
        user.setLoginId(userPojo.getLoginId());
        user.setName(userPojo.getName());
        user.setRole(userPojo.getRole());
        return user;
    }
}
