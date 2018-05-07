/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author dmytro
 */
public class Utils {
    
    public static ResponseEntity<SuccessCreateDto> buildCreateResponse(String memberId) {
        return ResponseEntity.ok(new SuccessCreateDto(memberId));
    }
    
    @ApiModel(description = "Success creation a new member")
    @ToString
    @EqualsAndHashCode
    public static class SuccessCreateDto {
        @ApiModelProperty(value = "ID of a created member", readOnly = true)
        public final String id;

        public SuccessCreateDto() {
            id ="";
        }

        public SuccessCreateDto(String id) {
            this.id = id;
        }
    }
    
    @ApiModel(description = "Login data")
    @ToString
    @EqualsAndHashCode
    public static class LoginData {
        @ApiModelProperty(value = "Login for admin", readOnly = true)
        public final String login;
        @ApiModelProperty(value = "Password for admin", readOnly = true)
        public final String password;

        public LoginData() {
            login = password = "";
        }

        public LoginData(String login, String password) {
            this.login = login;
            this.password = password;
        }        
    }
}
