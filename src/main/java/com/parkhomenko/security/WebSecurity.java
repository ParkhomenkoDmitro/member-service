/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 *
 * @author dmytro
 */

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    
     @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/members").permitAll()
                .antMatchers(HttpMethod.GET, "/members/get-all-list").permitAll()
                .anyRequest().authenticated();
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
