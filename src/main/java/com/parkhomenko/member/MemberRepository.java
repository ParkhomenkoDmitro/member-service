package com.parkhomenko.member;

import org.springframework.data.mongodb.repository.MongoRepository;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dmytro
 */
public interface MemberRepository extends MongoRepository<Member, String> {
    
}
