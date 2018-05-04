/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 *
 * @author dmytro
 */

@Component
public class MemberValidator {
    
    public boolean isAllValid(MemberDto memberDto) {
        boolean firstName = StringUtils.isEmpty(memberDto.firstName);
        boolean lastName = StringUtils.isEmpty(memberDto.lastName);
        boolean postalCode = StringUtils.isEmpty(memberDto.postalCode);
        boolean birthDate =  memberDto.birthDate == null || memberDto.birthDate.isAfter(LocalDate.now());
        
        if((firstName && lastName && postalCode && birthDate) == false) {
            return false;
        }
        
        Pattern compile = Pattern.compile("(^([0-9]{5})$)|(^([0-9]{6})$)");
        Matcher matcher = compile.matcher(memberDto.postalCode);
        boolean postalCodeValidation = matcher.find();
        
        if(postalCodeValidation == false) {
            return false;
        }
        
        //TODO
        
        return true;
    }
    
}
