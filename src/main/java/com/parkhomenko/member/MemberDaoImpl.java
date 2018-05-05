/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko.member;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author dmytro
 */
public class MemberDaoImpl implements MemberDao {
    
    @Autowired
    private MemberRepository memberRepository;

    @Override
    public String create(MemberDto memberDto) {
        Member newMember = new Member(memberDto.firstName, memberDto.lastName,
                memberDto.postalCode, memberDto.birthDate, memberDto.image);
        Member savedMember = memberRepository.save(newMember);
        return savedMember.id;
    }

    @Override
    public List<MemberDto> getAll() {
        return memberRepository.findAll().stream().map(item
                -> new MemberDto(item.id, item.firstName, item.lastName,
                        item.postalCode, item.birthDate, item.image))
                .collect(Collectors.toList());
    }

    @Override
    public MemberDto getOne(String id) {
        Optional<Member> optional = memberRepository.findById(id);

        if (optional.isPresent()) {
            Member member = optional.get();
            return new MemberDto(member.id, member.firstName, member.lastName,
                    member.postalCode, member.birthDate, member.image);
        } else {
            return new MemberDto();
        }
    }

    @Override
    public void update(MemberDto memberDto) {
        Optional<Member> optional = memberRepository.findById(memberDto.id);

        if (optional.isPresent()) {
            Member member = optional.get();
            
            member.birthDate = memberDto.birthDate;
            member.firstName = memberDto.firstName;
            member.lastName = memberDto.lastName;
            member.postalCode = memberDto.postalCode;
            member.image = memberDto.image;
            
            memberRepository.save(member);
        }
    }

    @Override
    public void delete(List<String> ids) {
        if(ids == null) {
            return;
        }
        
        ids.stream().forEach(id -> {
            memberRepository.deleteById(id);
        });
    }
}
