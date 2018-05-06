package com.parkhomenko.admin;

import com.parkhomenko.common.Constants;
import io.swagger.annotations.Api;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dmytro
 */

@RestController
@RequestMapping("/admins")
@Api(value="Member Server", produces = "application/json, application/xml")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private AdminValidator adminValidator;
    
    @Autowired
    private Environment env;
    
    @PostMapping("/logout")
    public void logout(HttpServletRequest req) {
        String jwtTokenHeaderName = env.getProperty(Constants.JWT_TOKEN_HEADER_NAME_KEY); 
        String jwtToken = req.getHeader(jwtTokenHeaderName);
        adminService.logout(jwtToken);
    }
    
    @PostMapping("/sign-up")
    public ResponseEntity signUpByPassword(@RequestBody Map<String, String> loginData) {
        if(adminValidator.singUpCheck(loginData) == false) {
            return ResponseEntity.badRequest().build();
        }

        final String newLogin = loginData.get(Constants.USERNAME_LOGIN_FORM_PARAMETER_KEY);
        final String pwd = loginData.get(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY);
        
        adminService.signUpByPassword(new AdminDto(newLogin, pwd));
        
        return ResponseEntity.ok().build();
    }
}
