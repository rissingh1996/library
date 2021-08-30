package com.rishabh.librarymanagement.controller;

import com.rishabh.librarymanagement.pojo.BookDetails;
import com.rishabh.librarymanagement.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


}
