package com.rishabh.librarymanagement.repository;

import com.rishabh.librarymanagement.dao.Book;
import com.rishabh.librarymanagement.dao.BookIssueHistory;
import com.rishabh.librarymanagement.dao.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookIssueHistoryRepository extends JpaRepository<BookIssueHistory, Long> {

    @Query("SELECT u FROM BookIssueHistory u where u.returnedDate is null and u.user.loginId = :loginId")
    List<BookIssueHistory> findCurrentlyIssuedBooksByLoginId(@Param("loginId") String loginId);

    @Query("Select u from BookIssueHistory u where u.user.loginId = :loginId")
    List<BookIssueHistory> findAllByLoginId(@Param("loginId") String loginId, Pageable pageable);

    @Query("SELECT u FROM BookIssueHistory u where u.returnedDate is null and u.book = :book and u.user.library.libraryCode = :libraryCode")
    List<BookIssueHistory> findAllByBookIdAndLibraryCodeAndReturnedDateIsNull(@Param("book") Book book, @Param("libraryCode") String libraryCode, Pageable pageable);

    @Query("SELECT u FROM BookIssueHistory u where u.returnedDate is null and u.user.library.libraryCode = :libraryCode")
    List<BookIssueHistory> findAllByLibraryCodeAndReturnedDateIsNull(@Param("libraryCode") String libraryCode, Pageable pageable);

    @Query("SELECT u FROM BookIssueHistory u where u.returnedDate is null and u.book = :book and u.user = :user")
    List<BookIssueHistory> findByBookIdAndUserAndReturnedDateIsNull(@Param("book") Book book, @Param("user") User user, Pageable pageable);

    List<BookIssueHistory> findAllByUser(User user, Pageable pageable);
}
