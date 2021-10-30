package com.rishabh.librarymanagement.controller;

import com.rishabh.librarymanagement.dao.User;
import com.rishabh.librarymanagement.pojo.AuthRequest;
import com.rishabh.librarymanagement.service.AuthService;
import com.rishabh.librarymanagement.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/get-token")
    ResponseEntity generateToken(@RequestBody AuthRequest authRequest) {
        try {
            return new ResponseEntity<Map<String, String>>(authService.authenticateAndGetResponse(authRequest), HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<String>("Invalid LoginId/LibraryCode", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping(value = "/hello")
    public String test() {
        return "Hello World!!!";

    }
}
