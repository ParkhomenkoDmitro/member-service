/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author dmytro
 */

@Service
public class AdminService {
    
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AdminDao adminDao;

    public AdminService(BCryptPasswordEncoder bCryptPasswordEncoder, AdminDao adminDao) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.adminDao = adminDao;
    }
    
    public void signUpByPassword(AdminDto dto) {
        String encodedPassword = bCryptPasswordEncoder.encode(dto.password);
        AdminDto customerWithHashedPassword = new AdminDto(dto.login, encodedPassword);
        
        adminDao.signUpByPassword(customerWithHashedPassword);
    }
    
     public void logout(String jwtToken) {
         adminDao.logout(jwtToken);
     }
}
