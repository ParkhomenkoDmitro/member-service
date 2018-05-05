/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkhomenko.AdminDao;
import com.parkhomenko.AdminDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 * @author dmytro
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final String TOKEN_PREFIX;
    private final String HEADER_STRING;
    private final String SECRET;
    private final long EXPIRATION_TIME;
    
    private final AdminDao adminDao;
    
    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, 
            String tokenPrefix, String headerString, String secret, 
            long expirationTime, AdminDao adminDao) {
        this.authenticationManager = authenticationManager;
        TOKEN_PREFIX = tokenPrefix;
        HEADER_STRING = headerString;
        SECRET = secret;
        EXPIRATION_TIME = expirationTime;
        this.adminDao = adminDao;
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            AdminDto creds = new ObjectMapper()
                    .readValue(req.getInputStream(), AdminDto.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.login,
                            creds.password,
                            new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {

        final String login = ((User) auth.getPrincipal()).getUsername();
        final String token = Jwts.builder()
                .setId(String.valueOf(System.nanoTime()))
                .setSubject(login)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
                .compact();
        
        adminDao.setToken(login, token);
        
        res.addHeader(HEADER_STRING, JWTAuthenticationFilter.buildAuthHeaderValue(TOKEN_PREFIX, token));
    }
    
    public static String buildAuthHeaderValue(String tokenPrefix, String jwtToken) {
        return tokenPrefix + "\u0020" + jwtToken;
    }
}
