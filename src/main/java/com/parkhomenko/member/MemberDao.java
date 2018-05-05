/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko.member;

import java.util.List;

/**
 *
 * @author dmytro
 */
public interface MemberDao {

    String create(MemberDto memberDto);

    List<MemberDto> getAll();

    MemberDto getOne(String id);
    
    void update(MemberDto memberDto);

    void delete(List<String> ids);
}
