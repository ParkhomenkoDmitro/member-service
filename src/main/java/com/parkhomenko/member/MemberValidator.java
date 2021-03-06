/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko.member;

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

    private static final int MAX_IMAGE_SIZE_MB = 1;
    private final MemberDao memberDao;

    public MemberValidator(MemberDao memberDao) {
        this.memberDao = memberDao;
    }
    
    /**
     * Germany postal code validation used
     *
     * @param memberDto
     * @return true if valid, false if invalid
     */
    public boolean isAllValidOnCreate(MemberDto memberDto) {
        final boolean firstNameValid
                = StringUtils.isEmpty(memberDto.firstName) == false;
        final boolean lastNameValid
                = StringUtils.isEmpty(memberDto.lastName) == false;
        final boolean postalCodeValid
                = StringUtils.isEmpty(memberDto.postalCode) == false;
        final boolean birthDateValid = memberDto.birthDate != null
                && (memberDto.birthDate.isAfter(LocalDate.MIN)
                && memberDto.birthDate.isBefore(LocalDate.now()));

        if ((firstNameValid && lastNameValid && postalCodeValid && birthDateValid) == false) {
            return false;
        }

        Pattern compile = Pattern.compile("(^([0-9]{5})$)|(^([0-9]{6})$)");
        Matcher matcher = compile.matcher(memberDto.postalCode);
        boolean postalCodeValidation = matcher.find();

        if (postalCodeValidation == false) {
            return false;
        }

        if(StringUtils.isEmpty(memberDto.image) == false) {
            int size = memberDto.image.getBytes().length;
            
            if((size / 1024 / 1024) > MAX_IMAGE_SIZE_MB) {
                return false;
            }
        }
        
        return true;
    }
    
     public boolean isAllValidOnUpdate(MemberDto memberDto) {
        MemberDto foundMember = memberDao.getOneById(memberDto.id);
        
        if(StringUtils.isEmpty(foundMember.id)) {
            return false;
        }
         
         return isAllValidOnCreate(memberDto) && StringUtils.isEmpty(memberDto.id) == false;
     }

}
