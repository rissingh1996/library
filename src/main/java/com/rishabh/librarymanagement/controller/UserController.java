package com.rishabh.librarymanagement.controller;

import com.rishabh.librarymanagement.dao.User;
import com.rishabh.librarymanagement.pojo.BookDto;
import com.rishabh.librarymanagement.pojo.UserPojo;
import com.rishabh.librarymanagement.pojo.UserHomeResponse;
import com.rishabh.librarymanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping(value = "/user")
    public HttpEntity<?> createUser(@RequestBody @Valid UserPojo userPojo) {
        try {
            return new ResponseEntity<User>(userService.createUser(userPojo), HttpStatus.CREATED);
        } catch (Exception exception) {
            return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(value = "/user/{id}")
    public HttpEntity<?> deleteUser(@PathVariable(value = "id") String userId) {
        try {
            userService.deleteUser(userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
