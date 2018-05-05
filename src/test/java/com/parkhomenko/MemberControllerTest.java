package com.parkhomenko;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

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

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Environment env;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Before
    public void setup() throws Exception {
        mongoTemplate.dropCollection(Member.class);
        mongoTemplate.dropCollection(Admin.class);
    }

    @Test
    public void unauthorized_create_application_json_test() throws Exception {
        MemberDto member = new MemberDto("Dima", "Parkhomenko", null, null, null);

        mockMvc.perform(post("/members")
                .content(objectMapper.writeValueAsString(member))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(401));
    }

    @Test
    public void unauthorized_get_all_application_josn_test() throws Exception {
        mockMvc.perform(get("/members/get-all-list")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    public void unauthorized_get_one_by_id_application_josn_test() throws Exception {
        mockMvc.perform(get("/members/123")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    public void unauthorized_update_one_by_id_application_josn_test() throws Exception {
        MemberDto member = new MemberDto("123", "Dima", "Parkhomenko", null, null, null);

        mockMvc.perform(put("/members")
                .content(objectMapper.writeValueAsString(member))
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    public void unauthorized_delete_by_ids_application_josn_test() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put(Constants.LIST_ID_KEY, Arrays.asList("1", "2", "3"));

        mockMvc.perform(delete("/members")
                .params(params)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    public void unauthorized_logout_application_json_test() throws Exception {
        String jwtTokenHeaderName = env.getProperty(Constants.JWT_TOKEN_HEADER_NAME_KEY);

        mockMvc.perform(post("/admins/logout")
                .header(jwtTokenHeaderName, "test jwt token")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(401));
    }

    @Test
    public void sing_up_for_admin_application_json_test() throws Exception {
        HashMap<String, String> data = new HashMap<String, String>() {
            {
                put(Constants.USERNAME_LOGIN_FORM_PARAMETER_KEY, "Dmytro");
                put(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY, "qwerty");
            }
        };
        
        Map<String, String> payload = Collections.unmodifiableMap(data);

        mockMvc.perform(post("/admins/sign-up")
                .content(objectMapper.writeValueAsString(payload))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200));
        
        MvcResult mvcResult = mockMvc.perform(post("/admins/login")
                .content(objectMapper.writeValueAsString(payload))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200)).andReturn();
        
        final String jwtToken = mvcResult.getResponse().getHeader(env.getProperty(Constants.JWT_TOKEN_HEADER_NAME_KEY));
        
        Assert.assertTrue("JWT token is NULL!", jwtToken != null);
        Assert.assertTrue("JWT token is empty!", StringUtils.isEmpty(jwtToken) == false);
        Assert.assertTrue("JWT token has invalid prefix!", 
                jwtToken != null && jwtToken.contains(
                        env.getProperty(Constants.JWT_TOKEN_PREFIX_KEY)));
    }

}
