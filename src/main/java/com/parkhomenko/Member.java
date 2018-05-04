/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko;

import java.io.Serializable;
import java.time.LocalDate;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.data.annotation.Id;

/**
 *
 * @author dmytro
 */

@XmlRootElement
public class Member implements Serializable {
 
    @Id
    public String id;
    public String firstName;
    public String lastName;
    public String postalCode;
    public LocalDate birthDate;
    public byte[] image;

    public Member() {
    }

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
