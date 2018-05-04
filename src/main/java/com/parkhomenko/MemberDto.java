/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko;

import java.time.LocalDate;

/**
 *
 * @author dmytro
 */
public class MemberDto {
    public final String id;
    public final String firstName;
    public final String lastName;
    public final String postalCode;
    public final LocalDate birthDate;
    public final byte[] image;

    public MemberDto(String firstName, String lastName, String postalCode, LocalDate birthDate, byte[] image) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.postalCode = postalCode;
        this.birthDate = birthDate;
        this.image = image;
        id = "";
    }

    public MemberDto(String id, 
            String firstName, 
            String lastName, 
            String postalCode, 
            LocalDate birthDate, 
            byte[] image) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.postalCode = postalCode;
        this.birthDate = birthDate;
        this.image = image;
    }
}
