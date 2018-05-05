/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author dmytro
 */
public class UnitJwtTest {
    
    @Test
    public void unique_token_generation_each_call_test() throws InterruptedException {
        int count = 0;
        String prev = "";
        
        for (int i = 0; i < 100; i++) {
            final String token = Jwts.builder()
                .setId(String.valueOf(System.nanoTime()))
                .setSubject("Dima")
                .setExpiration(new Date(System.currentTimeMillis() + 864000000))
                .signWith(SignatureAlgorithm.HS512, "SecretKeyToGenJWTs".getBytes())
                .compact();
            
            if(prev.equals(token)) {
                count++;
            }
            
            prev = token;            
        }
        
        Assert.assertEquals(0, count);
    }    
}
