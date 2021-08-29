package com.rishabh.librarymanagement.service;

import com.rishabh.librarymanagement.dao.User;
import com.rishabh.librarymanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findById(Long.valueOf(username)).get();
        return new org.springframework.security.core.userdetails.User(user.getLoginId().toString(), user.getLibrary().getLibraryCode(), new ArrayList<>());
    }
}
