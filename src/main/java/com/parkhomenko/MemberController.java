/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dmytro
 */

@RestController
@RequestMapping(value="/members")
public class MemberController {
    
    @Autowired
    private MemberValidator memberValidator;
    
    @Autowired
    private MemberService memberService;
    
    @GetMapping("/get-all-list")
    public List<MemberDto> getAlMembersList() {
        return memberService.getAll();
    }
    
    @GetMapping("/{id}")
    public MemberDto getOneMemberById(@PathVariable String id) {
        return memberService.getOne(id);
    }
    
    @PutMapping
    public ResponseEntity updateOneMemberById(@RequestBody MemberDto memberDto) {
        if(memberValidator.isAllValid(memberDto) == false) {
            return ResponseEntity.badRequest().build();
        }
        
        memberService.update(memberDto);
        
        return ResponseEntity.ok().build();
    }
    
    @PostMapping
    public ResponseEntity<Map<String, String>> createOneMemeber(@RequestBody MemberDto memberDto) {
        if(memberValidator.isAllValid(memberDto) == false) {
            return ResponseEntity.badRequest().build();
        }
        
        final String memberId = memberService.create(memberDto);
        HashMap<String, String> res = new HashMap<String, String>() {{
            put(Constants.ENTITY_ID_KEY, memberId);
        }};
        
        return ResponseEntity.ok(res);
    }
    
    @DeleteMapping
    public void deleteMembersByIds(@RequestParam(value = Constants.LIST_ID_KEY) List<String> ids) {
        memberService.delete(ids);
    }
}