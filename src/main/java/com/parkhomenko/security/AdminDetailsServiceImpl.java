/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko.security;

import com.parkhomenko.admin.AdminDao;
import com.parkhomenko.admin.AdminDto;
import static java.util.Collections.emptyList;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 *
 * @author dmytro
 */

@Service("AdminDetailsServiceImpl")
public class AdminDetailsServiceImpl implements UserDetailsService {

    private final AdminDao adminDao;

    public AdminDetailsServiceImpl(AdminDao adminDao) {
        this.adminDao = adminDao;
    }
    
    @Override
    public UserDetails loadUserByUsername(String adminLogin) throws UsernameNotFoundException {
        if(StringUtils.isEmpty(adminLogin)) {
            throw new UsernameNotFoundException(adminLogin);
        }
        
        AdminDto admin = adminDao.findByLogin(adminLogin);
        
        if(admin == null) {
            throw new UsernameNotFoundException(adminLogin);
        }
        
        return new User(admin.login, admin.password, emptyList());
    }
    
}
