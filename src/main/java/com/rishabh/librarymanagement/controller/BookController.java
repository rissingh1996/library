package com.rishabh.librarymanagement.controller;

import com.rishabh.librarymanagement.annotations.Authorization;
import com.rishabh.librarymanagement.dao.BookIssueHistory;
import com.rishabh.librarymanagement.pojo.BookAddDto;
import com.rishabh.librarymanagement.pojo.BookDetails;
import com.rishabh.librarymanagement.pojo.BookIssueReturnDto;
import com.rishabh.librarymanagement.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping(value = "/book/search")
    public HttpEntity<?> bookSearch(@RequestParam(required = false, defaultValue = "0") int page, @RequestParam(required = false, defaultValue = "3") int size,
                                    @RequestParam(name = "keyword") String keyword) {
        try {
            return new ResponseEntity<List<BookDetails>>(bookService.getRelevantBooks(page, size, keyword), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/book/details/{bookNo}")
    public HttpEntity<?> getBookDetails(@PathVariable("bookNo") String bookNo) {
        try {
            return new ResponseEntity<BookDetails>(bookService.getBookDetails(bookNo), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/book/recommendation")
    public HttpEntity<?> getBookRecommmendation(@RequestParam(value = "bookNo", required = false) String bookNo) {
        try {
            return new ResponseEntity<List<BookDetails>>(bookService.getBookRecommendation(bookNo), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Authorization(value = {"ADMIN"})
    @PostMapping(value = "/book")
    public HttpEntity<?> addBook(@RequestBody @Valid BookAddDto bookDetails) {
        try {
            bookService.addBook(bookDetails);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception exception) {
            return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Authorization(value = {"ADMIN"})
    @DeleteMapping(value = "/book/{bookNo}/{bookCount}")
    public HttpEntity<?> removeBook(@PathVariable(value = "bookNo") String bookNo, @PathVariable(value = "bookCount", required = false) Integer bookCount) {
        try {
            bookService.removeBook(bookNo, bookCount);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Authorization(value = {"ADMIN"})
    @GetMapping(value = "/issued/book")
    public HttpEntity<?> getIssuedBooks(@RequestParam(required = false, defaultValue = "0") int page,
                                        @RequestParam(required = false, defaultValue = "3") int size) {
        try {
            return new ResponseEntity<List<BookIssueHistory>>(bookService.getIssuedBooks(page, size), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Authorization(value = {"ADMIN"})
    @PostMapping(value = "/book/issue")
    public HttpEntity<?> bookIssue(@RequestBody @Valid BookIssueReturnDto bookIssueReturnDto) {
        try {
            return new ResponseEntity<BookIssueHistory>(bookService.issueBook(bookIssueReturnDto), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @Authorization(value = {"ADMIN"})
    @PostMapping(value = "/book/return")
    public HttpEntity<?> bookReturn(@RequestBody BookIssueReturnDto bookIssueReturnDto) {
        try {
            return new ResponseEntity<BookIssueHistory>(bookService.returnBook(bookIssueReturnDto), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<String>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
