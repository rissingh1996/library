package com.rishabh.librarymanagement.service;

import com.microsoft.sqlserver.jdbc.StringUtils;
import com.rishabh.librarymanagement.utils.CustomThreadLocal;
import com.rishabh.librarymanagement.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Service
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomThreadLocal customThreadLocal;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if(!StringUtils.isEmpty(authorizationHeader)) {
            String token = authorizationHeader.substring(7);
            if (jwtUtil.validateToken(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
                Claims claims = jwtUtil.extractAllClaims(token);
                SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(claims.get("loginId"), claims.get("libraryCode"))
                ));
                customThreadLocal.getCustomThreadLocal().set(claims);
            }
        }

        filterChain.doFilter(request, response);
    }

}
