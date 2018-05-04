/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko.security;

import com.parkhomenko.AdminDao;
import com.parkhomenko.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 *
 * @author dmytro
 */

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
    
    private static final String LOGIN_URL = "/admins/login";
    private static final String SIGN_UP_URL = "/admins/sign-up";

    @Autowired
    private Environment env;
    
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final AdminDao adminDao;
    
    public WebSecurity(UserDetailsService userDetailsService, 
            BCryptPasswordEncoder bCryptPasswordEncoder, 
            RestAuthenticationEntryPoint restAuthenticationEntryPoint, 
            AdminDao adminDao) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.adminDao = adminDao;
    }
    
     @Override
    protected void configure(HttpSecurity http) throws Exception {
        final JWTAuthenticationFilter jwtAuthenticationFilter = 
                new JWTAuthenticationFilter(authenticationManager(), 
                        env.getProperty(Constants.JWT_TOKEN_PREFIX_KEY),
                        env.getProperty(Constants.JWT_TOKEN_HEADER_NAME_KEY),
                        env.getProperty(Constants.JWT_TOKEN_SECRET_KEY), 
                        env.getProperty(Constants.JWT_TOKEN_EXPIRATIO_TIME_KEY, Long.class),
                        adminDao
                );
        
        jwtAuthenticationFilter.setUsernameParameter(Constants.USERNAME_LOGIN_FORM_PARAMETER_KEY);
        jwtAuthenticationFilter
                .setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(LOGIN_URL, "POST"));
        
        http.cors()
                .and()
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                .antMatchers(HttpMethod.POST, LOGIN_URL).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(jwtAuthenticationFilter)
                .addFilter(new JWTAuthorizationFilter(authenticationManager(),
                        env.getProperty(Constants.JWT_TOKEN_PREFIX_KEY),
                        env.getProperty(Constants.JWT_TOKEN_HEADER_NAME_KEY),
                        env.getProperty(Constants.JWT_TOKEN_SECRET_KEY), 
                        adminDao))
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
    
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
    
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
