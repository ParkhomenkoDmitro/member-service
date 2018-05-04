/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 *
 * @author dmytro
 */

@Component
public class AdminValidator {
    
    private final AdminDao adminDao;

    public AdminValidator(AdminDao adminDao) {
        this.adminDao = adminDao;
    }
    
    public boolean singUpCheck(String newLogin) {
        AdminDto admin = adminDao.findByLogin(newLogin);
        return admin == null;
    }
    
}
