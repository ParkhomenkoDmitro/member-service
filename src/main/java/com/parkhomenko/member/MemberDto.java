/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 *
 * @author dmytro
 */

@ApiModel(value = "Member", description = "Member")
@ToString
@EqualsAndHashCode
public class MemberDto {
    @ApiModelProperty(value = "The unique identifier of the given member", readOnly = true)
    public final String id;
    @ApiModelProperty(value = "The first name of the given member", readOnly = true)
    public final String firstName;
    @ApiModelProperty(value = "The last name of the given member", readOnly = true)
    public final String lastName;
    @ApiModelProperty(value = "The postal code (ZIP) of the given member", readOnly = true)
    public final String postalCode;
    @ApiModelProperty(value = "The birthday of the given member", readOnly = true)
    public final LocalDate birthDate;
    @ApiModelProperty(value = "The image of the given member in Base64 format", readOnly = true)
    public final String image;

    public MemberDto() {
        id = firstName = lastName = postalCode = image = "";
        birthDate = LocalDate.MIN;
    }

    public MemberDto(String firstName, String lastName, String postalCode, 
            LocalDate birthDate, String image) {
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
            String image) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.postalCode = postalCode;
        this.birthDate = birthDate;
        this.image = image;
    }
}
