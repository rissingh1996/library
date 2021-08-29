package com.rishabh.librarymanagement;

import com.rishabh.librarymanagement.dao.BookIssueHistory;
import com.rishabh.librarymanagement.dao.Library;
import com.rishabh.librarymanagement.dao.User;
import com.rishabh.librarymanagement.repository.BookIssueHistoryRepository;
import com.rishabh.librarymanagement.repository.BookRepository;
import com.rishabh.librarymanagement.repository.LibraryRepository;
import com.rishabh.librarymanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.Date;

@SpringBootApplication
public class LibraryManagementApplication {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private LibraryRepository libraryRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private BookIssueHistoryRepository bookIssueHistoryRepository;

	public static void main(String[] args) {
		SpringApplication.run(LibraryManagementApplication.class, args);
	}

	@PostConstruct
	void init() {
		User user = userRepository.findById(1L).get();
		if(user != null) {
			Library library = libraryRepository.save(new Library(1L, "DEFAULT", "default", false, true, true, true, true, true, false));
			user = userRepository.save(new User(1L, library, "User"));
		}
		bookIssueHistoryRepository.save(new BookIssueHistory(2L, user, bookRepository.getById(1L), new Date(), new Date(), new Date()));
	}

}
