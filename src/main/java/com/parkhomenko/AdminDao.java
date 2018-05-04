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
public interface AdminDao {
    void signUpByPassword(AdminDto customerDto);
    AdminDto findByLogin(String login);
    void logout(String jwtToken);
    void setToken(String login, String jwtToken);
}
