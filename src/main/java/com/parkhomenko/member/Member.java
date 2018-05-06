/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko.member;

import java.io.Serializable;
import java.time.LocalDate;
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
public class Member implements Serializable {
    @Id
    public String id;
    @Version 
    public Long version;
    public String firstName;
    public String lastName;
    public String postalCode;
    public LocalDate birthDate;
    public byte[] image;

    public Member(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Member(String firstName, String lastName, String postalCode, LocalDate birthDate, byte[] image) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.postalCode = postalCode;
        this.birthDate = birthDate;
        this.image = image;
    }
}
