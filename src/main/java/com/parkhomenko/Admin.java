/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko;

import org.springframework.data.annotation.Id;

/**
 *
 * @author dmytro
 */
public class Admin {
    @Id
    public String id;
    public String login;
    public String password;
    public String token;
    
    public Admin(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
