package com.parkhomenko;

import com.parkhomenko.admin.AdminDto;
import com.parkhomenko.admin.AdminDao;
import com.mongodb.WriteConcern;
import com.parkhomenko.admin.AdminService;
import com.parkhomenko.member.MemberDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.WriteConcernResolver;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private AdminService adminService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        adminService.signUpByPassword(new AdminDto("hachiko", "hachiko"));
    }

    @Bean
    public WriteConcernResolver writeConcernResolver() {
        return action -> WriteConcern.ACKNOWLEDGED;
    }
    
    @Bean
    public MemberDto emptyMember() {
        return new MemberDto();
    }
    
}