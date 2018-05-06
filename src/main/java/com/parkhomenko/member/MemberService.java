/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko.member;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author dmytro
 */
@Service
public class MemberService {

    @Autowired
    private MemberDao memberDao;

    public String create(MemberDto memberDto) {
        return memberDao.create(memberDto);
    }

    public List<MemberDto> getAll() {
        return memberDao.getAll();
    }

    public MemberDto getOneById(String id) {
        return memberDao.getOneById(id);
    }

    void update(MemberDto memberDto) {
        memberDao.update(memberDto);
    }

    void delete(List<String> ids) {
        memberDao.delete(ids);
    }
}
