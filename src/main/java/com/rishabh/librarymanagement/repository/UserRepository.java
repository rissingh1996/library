package com.rishabh.librarymanagement.repository;

import com.rishabh.librarymanagement.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
