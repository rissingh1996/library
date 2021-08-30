package com.rishabh.librarymanagement.controller;

import com.rishabh.librarymanagement.pojo.BookDto;
import com.rishabh.librarymanagement.pojo.UserHomeResponse;
import com.rishabh.librarymanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/user/home")
    public HttpEntity<?> getUserHome() {
        try {
            return new ResponseEntity<UserHomeResponse>(userService.getUserHome(), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/user/history")
    public HttpEntity<?> getUserHistory(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "3") int size) {
        try {
            return new ResponseEntity<List<BookDto>>(userService.getUserHistory(page, size), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
