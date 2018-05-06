package com.parkhomenko.admin;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * @author dmytro
 */
@ToString
@EqualsAndHashCode
public class AdminDto {
    public final String id;
    public final String login;
    public final String password;
    public final String token;

    public AdminDto() {
        id = login = password = token = "";
    }
    
    public AdminDto(String login, String password) {
        this.login = login;
        this.password = password;
        id = "";
        token = "";
    }
    
    public AdminDto(String id, String login, String password, String token) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.token = token;
    }
}
