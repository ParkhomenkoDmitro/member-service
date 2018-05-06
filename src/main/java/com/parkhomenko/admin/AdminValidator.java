/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko.admin;

import com.parkhomenko.common.Constants;
import java.util.Map;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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

    public boolean singUpCheck(Map<String, String> loginData) {
        final String newLogin = loginData.get(Constants.USERNAME_LOGIN_FORM_PARAMETER_KEY);
        final String pwd = loginData.get(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY);
        
        if(StringUtils.isEmpty(newLogin) || StringUtils.isEmpty(pwd)) {
            return false;
        }

        AdminDto admin = adminDao.findByLogin(newLogin);
        return admin == null;
    }

}
