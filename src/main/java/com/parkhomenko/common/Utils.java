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
    
    public static ResponseEntity<Map<String, String>> buildCreateResponse(String memberId) {
        HashMap<String, String> res = new HashMap<String, String>() {{
            put(Constants.ENTITY_ID_KEY, memberId);
        }};
        
        return ResponseEntity.ok(res);
    }
}
