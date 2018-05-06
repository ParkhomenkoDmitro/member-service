package com.parkhomenko.admin;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

/**
 *
 * @author dmytro
 */

@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class Admin {
    @Id
    public String id;
    @Version 
    public Long version;
    public String login;
    public String password;
    public String token;
    
    public Admin(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
