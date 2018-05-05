/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author dmytro
 */
@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public String create(MemberDto memberDto) {
        Member newMember = new Member(memberDto.firstName, memberDto.lastName,
                memberDto.postalCode, memberDto.birthDate, memberDto.image);
        Member savedMember = memberRepository.save(newMember);
        return savedMember.id;
    }

    public List<MemberDto> getAll() {
        return memberRepository.findAll().stream().map(item
                -> new MemberDto(item.id, item.firstName, item.lastName,
                        item.postalCode, item.birthDate, item.image))
                .collect(Collectors.toList());
    }

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

    void update(MemberDto memberDto) {
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

    void delete(List<String> ids) {
        if(ids == null) {
            return;
        }
        
        ids.stream().forEach(id -> {
            memberRepository.deleteById(id);
        });
    }
}
