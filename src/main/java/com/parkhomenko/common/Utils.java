/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko.common;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author dmytro
 */
public class Utils {
    
    public static ResponseEntity<SuccessCreateDto> buildCreateResponse(String memberId) {
        return ResponseEntity.ok(new SuccessCreateDto(memberId));
    }
    
    public static class SuccessCreateDto {
        public final String id;

        public SuccessCreateDto(String id) {
            this.id = id;
        }
    }
}
