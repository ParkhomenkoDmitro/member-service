package com.parkhomenko.admin;

import com.parkhomenko.common.Constants;
import com.parkhomenko.common.Utils.LoginData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
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
@Api(
        value = "Member Server", 
        produces = "application/json, application/xml", 
        consumes = "application/json, application/xml", 
        tags = "Admin resourse", description = "Admin resourse API")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private AdminValidator adminValidator;
    
    @Autowired
    private Environment env;
    

    @ApiOperation(value = "Authentication admin", notes = "Here you can log in as admin",
            consumes = "application/json, application/xml", produces = "application/json, application/xml")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully login")      
    })
    @PostMapping("/login")
    public void fakeLoginForSwaggerDocumentation(@ApiParam(required = true, 
            name = "loginData", 
            value = "Login and password for authentication")
            @RequestBody LoginData loginData) {
        throw new IllegalStateException("It's implemented by Spring Security filter");
    }
    
    @ApiOperation(value = "Logout admin", notes = "Logout admin, if invalid jwt token was sent then nothing happen",
            consumes = "application/json, application/xml", produces = "application/json, application/xml")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully logout"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource")
    })
    @PostMapping("/logout")
    public void logout(HttpServletRequest req) {
        String jwtTokenHeaderName = env.getProperty(Constants.JWT_TOKEN_HEADER_NAME_KEY); 
        String jwtToken = req.getHeader(jwtTokenHeaderName);
        adminService.logout(jwtToken);
    }
    
    @ApiOperation(value = "Sign up admin", notes = "Register admin by password", response = Void.class,
            consumes = "application/json, application/xml", produces = "application/json, application/xml")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully registered"),
        @ApiResponse(code = 400, message = "Data for registration of a new admin are not valid"),
        @ApiResponse(code = 401, message = "You are not authorized to view the resource")
    })
    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUpByPassword(
            @ApiParam(required = true, name = "loginData", value = "Login and password for registration",
                    example = "http:8080/app/members?ids=1&ids=2&ids=3")
            @RequestBody LoginData loginData) {
        
        if(adminValidator.singUpCheck(loginData) == false) {
            return ResponseEntity.badRequest().build();
        }
        
        adminService.signUpByPassword(new AdminDto(loginData.login, loginData.password));
        
        return ResponseEntity.ok().build();
    }
}
