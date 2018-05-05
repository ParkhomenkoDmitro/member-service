/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parkhomenko;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.test.web.servlet.MvcResult;



/**
 *
 * @author dmytro
 */
public class TestsUtil {
    
    public static String getEntityId(MvcResult result, ObjectMapper mapper) throws IOException {
      String bodyAsString = result.getResponse().getContentAsString();
      Map<String, String> body = mapper.readValue(bodyAsString, new TypeReference<HashMap<String, String>>() {});
      return body.get(Constants.ENTITY_ID_KEY);
    }
    
    public static <T> T parceString(MvcResult result, Class<T> clazz, ObjectMapper mapper) throws IOException {
        String bodyAsString = result.getResponse().getContentAsString();
        return mapper.readValue(bodyAsString, clazz);
    }
}
