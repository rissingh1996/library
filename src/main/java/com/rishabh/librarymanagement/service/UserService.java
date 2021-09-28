package com.rishabh.librarymanagement.service;

import com.rishabh.librarymanagement.dao.BookIssueHistory;
import com.rishabh.librarymanagement.dao.Library;
import com.rishabh.librarymanagement.dao.User;
import com.rishabh.librarymanagement.pojo.BookDto;
import com.rishabh.librarymanagement.pojo.UserPojo;
import com.rishabh.librarymanagement.pojo.UserHomeResponse;
import com.rishabh.librarymanagement.repository.*;
import com.rishabh.librarymanagement.utils.CustomThreadLocal;
import com.rishabh.librarymanagement.utils.LibraryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LibraryUtils libraryUtils;

    public UserHomeResponse getUserHome() {
        String loginId = String.valueOf(customThreadLocal.getCustomThreadLocal().get().get("loginId"));
        String libraryCode = (String) customThreadLocal.getCustomThreadLocal().get().get("libraryCode");
        UserHomeResponse userHomeResponse = new UserHomeResponse();
        userHomeResponse.setLibrary(libraryRepository.findByLibraryCode(libraryCode));
        List<BookIssueHistory> bookIssueHistoryList = bookIssueHistoryRepository.findCurrentlyIssuedBooksByLoginId(loginId);
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
        String loginId = String.valueOf(customThreadLocal.getCustomThreadLocal().get().get("loginId"));
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

    public User createUser(UserPojo userPojo) {
        Library library = libraryRepository.findByLibraryCode(userPojo.getLibraryCode());
        if (library == null)
            throw new RuntimeException("LibraryCode Not Found!!!");
        User user = libraryUtils.getUserDao(userPojo, library);
        return userRepository.save(user);
    }

    public void deleteUser(String userId) {
        Optional<User> user = userRepository.findById(userId);
        if (!user.isPresent())
            throw new RuntimeException("User Not Found!!!");
        userRepository.delete(user.get());
    }
}
