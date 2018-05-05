package com.parkhomenko.admin;

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
