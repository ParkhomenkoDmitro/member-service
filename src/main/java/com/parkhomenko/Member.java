/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko;

import java.io.Serializable;
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

    public Member() {
    }

    public Member(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
