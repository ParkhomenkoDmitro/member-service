/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko.security;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

import static com.parkhomenko.common.Constants.ERROR_MESSAGE_KEY;

/**
 *
 * @author dmytro
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final String MSG = "Unauthorized request: invalid token";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        handleAuthException(request, response, HttpServletResponse.SC_UNAUTHORIZED, MSG);
    }

    public static void handleAuthException(HttpServletRequest request, HttpServletResponse response, int status, String MSG) throws IOException {
        final String contentType = request.getContentType();
        final String acceptHeaderValue = request.getHeader("Accept");

        if (MediaType.APPLICATION_JSON_UTF8_VALUE.equals(contentType) || MediaType.APPLICATION_JSON_UTF8_VALUE.equals(acceptHeaderValue)) {
            response.setStatus(status);
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setCharacterEncoding("UTF-8");
            JSONObject payload = new JSONObject();
            payload.put(ERROR_MESSAGE_KEY, MSG);
            response.getWriter().write(payload.toString());

            return;
        } else {
            if (MediaType.APPLICATION_XML_VALUE.equals(contentType) || MediaType.APPLICATION_XML_VALUE.equals(acceptHeaderValue)) {
                response.setStatus(status);
                response.setContentType(MediaType.APPLICATION_XML_VALUE);
                response.setCharacterEncoding("UTF-8");

                HashMap<String, String> payload = new HashMap<String, String>() {
                    {
                        put(ERROR_MESSAGE_KEY, MSG);
                    }
                };

                XmlMapper xmlMapper = new XmlMapper();
                String xml = xmlMapper.writeValueAsString(payload);
                response.getWriter().write(xml);

                return;
            }
        }

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, MSG);
    }

}
