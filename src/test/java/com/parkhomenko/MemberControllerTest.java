package com.parkhomenko;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.nio.charset.Charset;
import javax.servlet.http.Cookie;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author dmytro
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MemberControllerTest {
    
//    private static final MediaType CONTENT_TYPE = new MediaType(
//            MediaType.APPLICATION_JSON.getType(),
//            MediaType.APPLICATION_JSON.getSubtype(), 
//            Charset.forName("utf8"));
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    public void create_application_json_test() throws Exception {
        Member member = new Member("Dima", "Parkhomenko");
        
        mockMvc.perform(post("/members")
                .content(objectMapper.writeValueAsString(member))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }
    
    @Test
    public void get_all_application_xml_test() throws Exception {
//        create_application_json_test();
        Member member = new Member("Dima_2", "Parkhomenko_2");
        
        XmlMapper xmlMapper = new XmlMapper();
        
        mockMvc.perform(post("/members")
                .content(xmlMapper.writeValueAsString(member))
                .contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk());
                
        mockMvc.perform(get("/members/get-all-list")
                .accept(MediaType.APPLICATION_XML))
                .andDo(print())
                .andExpect(status().isOk());
    }
    
}
