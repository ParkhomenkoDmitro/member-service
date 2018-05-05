/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko.security;

import com.parkhomenko.AdminDao;
import com.parkhomenko.AdminDto;
import io.jsonwebtoken.Jwts;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 *
 * @author dmytro
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    
    private final String TOKEN_PREFIX;
    private final String HEADER_STRING;
    private final String SECRET;

    private final AdminDao adminDao;
    
    public JWTAuthorizationFilter(AuthenticationManager authManager, 
            String tokenPrefix, String headerName, String secret, AdminDao adminDao) {
        super(authManager);
        TOKEN_PREFIX = tokenPrefix;
        HEADER_STRING = headerName;
        SECRET = secret;
        this.adminDao = adminDao;
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }
    
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        final String notTrustToken = request.getHeader(HEADER_STRING);
        
        if (notTrustToken != null) {
            // parse the token.
            final String login = Jwts.parser()
                    .setSigningKey(SECRET.getBytes())
                    .parseClaimsJws(notTrustToken.replace(TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject();

            if (login != null) {
                AdminDto admin = adminDao.findByLogin(login);
                
                if(admin == null) {
                    return null;
                } else {
                    final String trustToken = JWTAuthenticationFilter.buildAuthHeaderValue(TOKEN_PREFIX, admin.token);
                    
                    if(notTrustToken.equals(trustToken) == false) {
                        return null;
                    }
                }
                
                return new UsernamePasswordAuthenticationToken(login, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}
