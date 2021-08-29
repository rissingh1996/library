package com.rishabh.librarymanagement.repository;

import com.rishabh.librarymanagement.dao.Library;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, Long> {
    Library findByLibraryCode(String libraryCode);
}
