/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author dmytro
 */

@Repository
public class AdminDaoImpl implements AdminDao {

    private final AdminRepository adminRepository;

    public AdminDaoImpl(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }
    
    @Override
    public void signUpByPassword(AdminDto adminDto) {
        adminRepository.save(new Admin(adminDto.login, adminDto.password));
    }

    @Override
    public AdminDto findByLogin(String login) {
        Admin admin = adminRepository.findByLogin(login);
        return admin == null ? null : new AdminDto(admin.id, admin.login, admin.password, admin.token);
    }

    @Override
    public void logout(String jwtToken) {
        Admin admin = adminRepository.findByToken(jwtToken);
        
        if(admin == null) {
            return;
        }
        
        admin.token = null;
        adminRepository.save(admin);
    }

    @Override
    public void setToken(String login, String jwtToken) {
        Admin admin = adminRepository.findByLogin(login);
        admin.token = jwtToken;
        adminRepository.save(admin);
        System.out.println("!!!!!!!!!!!!!!!!!!! save token = " + jwtToken);
    }
}
