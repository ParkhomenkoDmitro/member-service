/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.parkhomenko.security.RestAuthenticationEntryPoint.handleAuthException;

/**
 *
 * @author dmytro
 */

@Component
public class RestAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private static final String MSG = "Invalid authentication data";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        handleAuthException(request, response, HttpServletResponse.SC_BAD_REQUEST, MSG);
    }
    
}
