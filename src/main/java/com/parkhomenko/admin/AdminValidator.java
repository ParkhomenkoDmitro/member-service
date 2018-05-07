/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko.admin;

import com.parkhomenko.common.Utils.LoginData;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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

    public boolean singUpCheck(LoginData loginData) {
        final String newLogin = loginData.login;
        final String pwd = loginData.password;
        
        if(StringUtils.isEmpty(newLogin) || StringUtils.isEmpty(pwd)) {
            return false;
        }

        AdminDto admin = adminDao.findByLogin(newLogin);
        return admin == null;
    }

}
