package com.rishabh.librarymanagement.service;

import com.rishabh.librarymanagement.dao.User;
import com.rishabh.librarymanagement.pojo.AuthRequest;
import com.rishabh.librarymanagement.repository.UserRepository;
import com.rishabh.librarymanagement.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public Map<String, String> authenticateAndGetResponse(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getLoginId(), authRequest.getLibraryCode())
        );
        User user = userRepository.getById(authRequest.getLoginId());
        Map<String, String> result = new HashMap<>();
        result.put("Access-token", jwtUtil.generateToken(user.getLoginId(), user.getLibrary().getLibraryCode()));
        result.put("Role", user.getRole());
        return result;
    }
}
