/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dmytro
 */

@RestController
@RequestMapping("/admins")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private AdminValidator adminValidator;
    
    @PostMapping("/logout")
    public void logout(@RequestHeader(value=Constants.JWT_TOKEN_HEADER_NAME_KEY) String jwtToken) {
        adminService.logout(jwtToken);
    }
    
    @PostMapping("/sign-up")
    public ResponseEntity signUpByPassword(@RequestBody Map<String, String> loginData) {
        final String newLogin = loginData.get(Constants.USERNAME_LOGIN_FORM_PARAMETER_KEY); 
        
        if(adminValidator.singUpCheck(newLogin) == false) {
            return ResponseEntity.badRequest().build();
        }
        
        adminService.signUpByPassword(new AdminDto(newLogin, 
                loginData.get(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY)
        ));
        
        return ResponseEntity.ok().build();
    }
}
