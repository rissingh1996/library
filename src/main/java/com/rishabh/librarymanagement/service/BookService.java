package com.rishabh.librarymanagement.service;

import com.rishabh.librarymanagement.dao.Book;
import com.rishabh.librarymanagement.dao.BookInventory;
import com.rishabh.librarymanagement.dao.BookIssueHistory;
import com.rishabh.librarymanagement.dao.User;
import com.rishabh.librarymanagement.pojo.BookAddDto;
import com.rishabh.librarymanagement.pojo.BookDetails;
import com.rishabh.librarymanagement.pojo.BookIssueDto;
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

import java.util.*;

import static com.rishabh.librarymanagement.utils.LibraryUtils.getBook;
import static com.rishabh.librarymanagement.utils.LibraryUtils.getBookInventory;

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
            List<BookIssueHistory> bookIssueHistoryList = bookIssueHistoryRepository.findAllByBookIdAndLibraryCodeAndReturnedDateIsNull(book, libraryCode, PageRequest.of(0, 1, Sort.by("dueDate").descending()));
            bookDetails.setReturnDate(!CollectionUtils.isEmpty(bookIssueHistoryList) ? bookIssueHistoryList.get(0).getDueDate() : null);
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

    public void addBook(BookAddDto bookDetails) {
        Book book = bookRepository.findByBookNo(bookDetails.getBookNo());
        if (book == null) {
            book = bookRepository.save(getBook(bookDetails));
        }
        BookInventory bookInventory = bookInventoryRepository.findByBookAndLibraryCode(book, bookDetails.getLibraryCode());
        if (bookInventory == null) {
            bookInventoryRepository.save(getBookInventory(bookDetails, book));
        } else {
            bookInventory.setBookCount(bookInventory.getBookCount() + bookDetails.getBookCount());
            bookInventoryRepository.save(bookInventory);
        }
    }

    public void removeBook(String bookNo, Integer bookCount) {
        String libraryCode = (String) customThreadLocal.getCustomThreadLocal().get().get("libraryCode");
        Book book = bookRepository.findByBookNo(bookNo);
        if (book == null)
            return;
        if (bookCount == null || bookCount == 0) {
            List<BookInventory> bookInventoryList = bookInventoryRepository.findByBook(book);
            Optional<BookInventory> bookInventory = bookInventoryList.stream().filter(bi -> Objects.equals(bi.getLibraryCode(), libraryCode)).findFirst();
            if (bookInventory.isPresent()) {
                bookInventory.get().setBookCount(0);
                bookInventoryRepository.save(bookInventory.get());
            }
        } else {
            List<BookInventory> bookInventoryList = bookInventoryRepository.findByBook(book);
            Optional<BookInventory> bookInventory = bookInventoryList.stream().filter(bi -> Objects.equals(bi.getLibraryCode(), libraryCode)).findFirst();
            if (bookInventory.isPresent()) {
                BookInventory bookInventory1 = bookInventory.get();
                if (bookInventory1.getBookCount() - bookCount < 0) {
                    bookInventory1.setBookCount(0);
                } else {
                    bookInventory1.setBookCount(bookInventory1.getBookCount() - bookCount);
                }
                bookInventoryRepository.save(bookInventory1);
            }
        }
    }

    public List<BookIssueHistory> getIssuedBooks(int page, int size) {
        String libraryCode = (String) customThreadLocal.getCustomThreadLocal().get().get("libraryCode");
        List<BookIssueHistory> bookIssueHistoryList = bookIssueHistoryRepository.findAllByLibraryCodeAndReturnedDateIsNull(libraryCode, PageRequest.of(page, size, Sort.by("dueDate").descending()));
        return bookIssueHistoryList;
    }

    public BookIssueHistory issueBook(BookIssueDto bookIssueDto) {
        String libraryCode = (String) customThreadLocal.getCustomThreadLocal().get().get("libraryCode");
        Book book = bookRepository.findByBookNo(bookIssueDto.getBookNo());
        if (book == null)
            throw new RuntimeException("Book Not Found!!!");
        BookInventory bookInventory = bookInventoryRepository.findByBookAndLibraryCode(book, libraryCode);
        if (bookInventory == null || bookInventory.getBookCount() == 0)
            throw new RuntimeException("Book Inventory Not Exist!!!");
        Optional<User> user = userRepository.findById(bookIssueDto.getUserId());
        if (!user.isPresent())
            throw new RuntimeException("User Not Exist!!!");
        bookInventory.setBookCount(bookInventory.getBookCount() - 1);
        bookInventoryRepository.save(bookInventory);
        BookIssueHistory bookIssueHistory = new BookIssueHistory();
        bookIssueHistory.setBook(book);
        bookIssueHistory.setIssuedDate(new Date(System.currentTimeMillis()));
        bookIssueHistory.setDueDate(new Date(System.currentTimeMillis() + 1209600000));
        bookIssueHistory.setUser(user.get());
        return bookIssueHistoryRepository.save(bookIssueHistory);
    }

    public BookIssueHistory returnBook(BookIssueDto bookIssueDto) {
        String libraryCode = (String) customThreadLocal.getCustomThreadLocal().get().get("libraryCode");
        Optional<User> user = userRepository.findById(bookIssueDto.getUserId());
        if (!user.isPresent())
            throw new RuntimeException("User Not Exist!!!");
        Book book = bookRepository.findByBookNo(bookIssueDto.getBookNo());
        if (book == null)
            throw new RuntimeException("Book Not Found!!!");
        List<BookIssueHistory> bookIssueHistoryList = bookIssueHistoryRepository.findByBookIdAndUserAndReturnedDateIsNull(book, user.get(),
                PageRequest.of(0, 1, Sort.by("dueDate")));
        if (CollectionUtils.isEmpty(bookIssueHistoryList))
            throw new RuntimeException("No Record Found!!!");
        bookIssueHistoryList.get(0).setReturnedDate(new Date(System.currentTimeMillis()));
        BookInventory bookInventory = bookInventoryRepository.findByBookAndLibraryCode(book, libraryCode);
        bookInventory.setBookCount(bookInventory.getBookCount() + 1);
        bookInventoryRepository.save(bookInventory);
        return bookIssueHistoryRepository.save(bookIssueHistoryList.get(0));
    }
}
