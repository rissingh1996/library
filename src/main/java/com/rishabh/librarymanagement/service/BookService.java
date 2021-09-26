package com.rishabh.librarymanagement.service;

import com.rishabh.librarymanagement.dao.Book;
import com.rishabh.librarymanagement.dao.BookInventory;
import com.rishabh.librarymanagement.dao.BookIssueHistory;
import com.rishabh.librarymanagement.pojo.BookDetails;
import com.rishabh.librarymanagement.repository.BookInventoryRepository;
import com.rishabh.librarymanagement.repository.BookIssueHistoryRepository;
import com.rishabh.librarymanagement.repository.BookRepository;
import com.rishabh.librarymanagement.repository.UserRepository;
import com.rishabh.librarymanagement.utils.CustomThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private CustomThreadLocal customThreadLocal;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookInventoryRepository bookInventoryRepository;

    @Autowired
    private BookIssueHistoryRepository bookIssueHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    public List<BookDetails> getRelevantBooks(int page, int size, String keyword) {
        List<Book> bookList = bookRepository.findAllByBookNameOrAuthorOrCategory(keyword, PageRequest.of(page, size));
        return new ArrayList<>(getBookDetailList(bookList));
    }

    private BookDetails getBookDetails(Book book) {
        String libraryCode = (String) customThreadLocal.getCustomThreadLocal().get().get("libraryCode");
        BookDetails bookDetails = new BookDetails();
        bookDetails.setBook(book);
        BookInventory bookInventory = bookInventoryRepository.findByBookAndLibraryCode(book, libraryCode);
        if (bookInventory != null && bookInventory.getBookCount() > 0) {
            bookDetails.setIsAvailable(true);
        } else {
            bookDetails.setIsAvailable(false);
            List<BookIssueHistory> bookIssueHistoryList = bookIssueHistoryRepository.findAllByBookIdAndLibraryCodeAndReturnedDateIsNull(book, libraryCode, PageRequest.of(0, 1, Sort.by("returnedDate").descending()));
            bookDetails.setReturnDate(!CollectionUtils.isEmpty(bookIssueHistoryList) ? bookIssueHistoryList.get(0).getReturnedDate() : null);
        }
        return bookDetails;
    }

    public BookDetails getBookDetails(String bookNo) {
        Book book = bookRepository.findByBookNo(bookNo);
        return getBookDetails(book);
    }

    public List<BookDetails> getBookRecommendation(String bookNo) {
        int count = 5;
        String loginId = (String) customThreadLocal.getCustomThreadLocal().get().get("loginId");
        Book book = null;
        if (StringUtils.isEmpty(bookNo)) {
            List<BookIssueHistory> bookIssueHistoryList = bookIssueHistoryRepository.findAllByUser(userRepository.getById(loginId), PageRequest.of(0, 1, Sort.by("issuedDate").descending()));
            bookNo = CollectionUtils.isEmpty(bookIssueHistoryList) ? null : bookIssueHistoryList.get(0).getBook().getBookNo();
            book = CollectionUtils.isEmpty(bookIssueHistoryList) ? null : bookIssueHistoryList.get(0).getBook();
        } else {
            book = bookRepository.findByBookNo(bookNo);
        }

        if (book == null) {
            bookNo = "02RS0001";
            book = bookRepository.findByBookNo(bookNo);
        }
        List<String> bookNoList = new ArrayList<>();
        bookNoList.add(bookNo);
        List<Book> bookListByAuthor = bookRepository.findByAuthorAndNotInBookNo(book.getAuthor(), bookNoList, PageRequest.of(0, 2));
        List<BookDetails> bookDetailsList = new ArrayList<>(getBookDetailList(bookListByAuthor));
        addToBookNoList(bookNoList, bookListByAuthor);
        count = count - bookListByAuthor.size();
        List<Book> bookListByGenres = bookRepository.findByBookNoAndNotInBookNo(bookNo.substring(0, 4), bookNoList, PageRequest.of(0, count));
        addToBookNoList(bookNoList, bookListByGenres);
        bookDetailsList.addAll(getBookDetailList(bookListByGenres));
        count = count - bookListByGenres.size();
        if (count > 0) {
            List<Book> bookListByGenre1 = bookRepository.findByBookNoAndNotInBookNo(bookNo.substring(0, 2), bookNoList, PageRequest.of(0, count));
            addToBookNoList(bookNoList, bookListByGenre1);
            bookDetailsList.addAll(getBookDetailList(bookListByGenre1));
            if (bookListByGenre1.size() != count) {
                List<Book> bookListByGenre2 = bookRepository.findByBookNoAndNotInBookNo2(bookNo.substring(0, 2), bookNoList, PageRequest.of(0, count));
                addToBookNoList(bookNoList, bookListByGenre2);
                bookDetailsList.addAll(getBookDetailList(bookListByGenre2));
            }
        }
        return bookDetailsList;
    }

    private void addToBookNoList(List<String> bookNoList, List<Book> bookList) {
        for (Book book : bookList) {
            bookNoList.add(book.getBookNo());
        }
    }

    private List<BookDetails> getBookDetailList(List<Book> bookList) {
        List<BookDetails> bookDetailsList = new ArrayList<>();
        for (Book book : bookList) {
            bookDetailsList.add(getBookDetails(book));
        }
        return bookDetailsList;
    }
}
