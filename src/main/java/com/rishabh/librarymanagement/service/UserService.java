package com.rishabh.librarymanagement.service;

import com.rishabh.librarymanagement.dao.BookIssueHistory;
import com.rishabh.librarymanagement.pojo.BookDto;
import com.rishabh.librarymanagement.pojo.UserHomeResponse;
import com.rishabh.librarymanagement.repository.BookInventoryRepository;
import com.rishabh.librarymanagement.repository.BookIssueHistoryRepository;
import com.rishabh.librarymanagement.repository.BookRepository;
import com.rishabh.librarymanagement.repository.LibraryRepository;
import com.rishabh.librarymanagement.utils.CustomThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private CustomThreadLocal customThreadLocal;

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private BookIssueHistoryRepository bookIssueHistoryRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookInventoryRepository bookInventoryRepository;

    public UserHomeResponse getUserHome() {
        Long loginId = Integer.toUnsignedLong((Integer) customThreadLocal.getCustomThreadLocal().get().get("loginId"));
        String libraryCode = (String) customThreadLocal.getCustomThreadLocal().get().get("libraryCode");
        UserHomeResponse userHomeResponse = new UserHomeResponse();
        userHomeResponse.setLibrary(libraryRepository.findByLibraryCode(libraryCode));
        List<BookIssueHistory> bookIssueHistoryList = bookIssueHistoryRepository.findCurrentlyIssuedBooksByUserId(loginId);
        List<BookDto> bookDtos = new ArrayList<>();
        for (BookIssueHistory bookIssueHistory : bookIssueHistoryList) {
            BookDto bookDto = new BookDto();
            bookDto.setBook(bookIssueHistory.getBook());
            bookDto.setIssuedDate(bookIssueHistory.getIssuedDate());
            bookDto.setDueDate(bookIssueHistory.getDueDate());
            bookDtos.add(bookDto);
        }
        userHomeResponse.setIssuedBooks(bookDtos);
        return userHomeResponse;
    }

    public List<BookDto> getUserHistory(int page, int size) {
        Long loginId = Integer.toUnsignedLong((Integer) customThreadLocal.getCustomThreadLocal().get().get("loginId"));
        List<BookIssueHistory> bookIssueHistoryList = bookIssueHistoryRepository.findAllByLoginId(loginId, PageRequest.of(page, size, Sort.by("issuedDate").descending()));
        List<BookDto> bookDtos = new ArrayList<>();
        for (BookIssueHistory bookIssueHistory : bookIssueHistoryList) {
            BookDto bookDto = new BookDto();
            bookDto.setBook(bookIssueHistory.getBook());
            bookDto.setIssuedDate(bookIssueHistory.getIssuedDate());
            bookDto.setDueDate(bookIssueHistory.getDueDate());
            bookDto.setReturnedDate(bookIssueHistory.getReturnedDate());
            bookDtos.add(bookDto);
        }
        return bookDtos;
    }
}
