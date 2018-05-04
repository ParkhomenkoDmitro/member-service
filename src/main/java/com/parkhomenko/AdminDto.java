/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko;

/**
 *
 * @author dmytro
 */
public class AdminDto {
    public final String id;
    public final String login;
    public final String password;
    public final String token;

    //For login
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
