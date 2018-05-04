/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dmytro
 */

@RestController
@RequestMapping(value="/members")
public class MemberController {
    
    @Autowired
    private MemberRepository memberRepository;
   
    @GetMapping("/get-all-list")
    public List<Member> getAll() {
        return memberRepository.findAll();
    }
    
    @PostMapping
    public void create(@RequestBody Member member) {
        memberRepository.save(member);
    }
    
    @PostMapping("/sign-up")
    public void signUpByPassword(@RequestBody Map<String, String> loginData) {
        //TODO
    }
    
}
