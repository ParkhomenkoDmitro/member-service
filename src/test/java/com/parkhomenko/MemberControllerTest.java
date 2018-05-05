package com.parkhomenko;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDate;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import static org.springframework.test.web.servlet.result.JsonPathResultMatchers.*;
import static org.hamcrest.Matchers.*;

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
        final MemberDto member = new MemberDto("Dima", "Parkhomenko", null, null, null);

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
        final MemberDto member = new MemberDto("123", "Dima", "Parkhomenko", null, null, null);

        mockMvc.perform(put("/members")
                .content(objectMapper.writeValueAsString(member))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    public void unauthorized_delete_by_ids_application_josn_test() throws Exception {
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put(Constants.LIST_ID_KEY, Arrays.asList("1", "2", "3"));

        mockMvc.perform(delete("/members")
                .params(params)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(401));
    }

    @Test
    public void unauthorized_logout_application_json_test() throws Exception {
        final String jwtTokenHeaderName = getAuthHeaderName();

        mockMvc.perform(post("/admins/logout")
                .header(jwtTokenHeaderName, "test jwt token")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(401));
    }

    @Test
    public void sing_up_for_admin_application_json_test() throws Exception {
        final Map<String, String> payload = buildLoginData();

        mockMvc.perform(post("/admins/sign-up")
                .content(objectMapper.writeValueAsString(payload))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200));

        MvcResult mvcResult = mockMvc.perform(post("/admins/login")
                .content(objectMapper.writeValueAsString(payload))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200)).andReturn();

        final String jwtToken = getTokenFromMvcResult(mvcResult);

        Assert.assertTrue("JWT token is NULL!", jwtToken != null);
        Assert.assertTrue("JWT token is empty!", StringUtils.isEmpty(jwtToken) == false);
        Assert.assertTrue("JWT token has invalid prefix!",
                jwtToken != null && jwtToken.contains(
                        env.getProperty(Constants.JWT_TOKEN_PREFIX_KEY)));
    }

    @Test
    public void double_login_check_for_admin_application_json_test() throws Exception {
        final String jwtTokenHeaderName = getAuthHeaderName();
        final Map<String, String> payload = buildLoginData();

        mockMvc.perform(post("/admins/sign-up")
                .content(objectMapper.writeValueAsString(payload))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200));

        MvcResult mvcResultAfterFirstLogin = mockMvc.perform(post("/admins/login")
                .content(objectMapper.writeValueAsString(payload))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200)).andReturn();

        final String jwtTokenAfterFirstLogin = getTokenFromMvcResult(mvcResultAfterFirstLogin);

        MvcResult mvcResultAfterSecondtLogin = mockMvc.perform(post("/admins/login")
                .content(objectMapper.writeValueAsString(payload))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200)).andReturn();

        final String jwtTokenAfterSecondLogin = getTokenFromMvcResult(mvcResultAfterSecondtLogin);

        mockMvc.perform(get("/members/get-all-list")
                .header(jwtTokenHeaderName, jwtTokenAfterFirstLogin)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(401));

        mockMvc.perform(get("/members/get-all-list")
                .header(jwtTokenHeaderName, jwtTokenAfterSecondLogin)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(200));
    }

    @Test
    public void success_login_for_admin_with_create_member_application_json_test() throws Exception {
        final String jwtTokenHeaderName = getAuthHeaderName();
        final String jwtToken = doAdminLoginUtil();
        final MemberDto originMember = buildTestMember();

        String entityId = doCreateMamberOnServerTest(jwtTokenHeaderName, jwtToken, originMember);

        MvcResult mvcResultGet = mockMvc.perform(get("/members/" + entityId)
                .header(jwtTokenHeaderName, jwtToken)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(200)).andReturn();

        final MemberDto fetchedMember = TestsUtil.parceString(mvcResultGet, MemberDto.class, objectMapper);
        compareMembers(originMember, fetchedMember);
    }

    @Test
    public void success_login_for_admin_with_put_member_application_json_test() throws Exception {
        final String jwtTokenHeaderName = getAuthHeaderName();
        final String jwtToken = doAdminLoginUtil();
        final MemberDto originMember = buildTestMember();

        String entityId = doCreateMamberOnServerTest(jwtTokenHeaderName, jwtToken, originMember);

        MvcResult mvcResultGet = mockMvc.perform(get("/members/" + entityId)
                .header(jwtTokenHeaderName, jwtToken)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(200)).andReturn();

        final MemberDto fetchedMember = TestsUtil.parceString(mvcResultGet, MemberDto.class, objectMapper);

        compareMembers(originMember, fetchedMember);

        final MemberDto originUpdatedMemberDto = new MemberDto(fetchedMember.id,
                "Nicolas", "Verba",
                "54321",
                LocalDate.of(1994, 5, 2),
                fetchedMember.image);

        mockMvc.perform(put("/members")
                .header(jwtTokenHeaderName, jwtToken)
                .content(objectMapper.writeValueAsString(originUpdatedMemberDto))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(200));

        MvcResult mvcResultGetAfterUpdate = mockMvc.perform(get("/members/" + entityId)
                .header(jwtTokenHeaderName, jwtToken)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(200)).andReturn();

        final MemberDto fetchedMemberAfterUpdate = TestsUtil.parceString(mvcResultGetAfterUpdate, MemberDto.class, objectMapper);

        compareMembers(originUpdatedMemberDto, fetchedMemberAfterUpdate);
    }

    private String doCreateMamberOnServerTest(String jwtTokenHeaderName,
            String jwtToken,
            MemberDto originMember) throws IOException, Exception {
        MvcResult mvcResultPost = mockMvc.perform(post("/members")
                .header(jwtTokenHeaderName, jwtToken)
                .content(objectMapper.writeValueAsString(originMember))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(200)).andReturn();
        String entityId = TestsUtil.getEntityId(mvcResultPost, objectMapper);
        return entityId;
    }

    @Test
    public void success_login_for_admin_with_get_all_members_application_json_test() throws Exception {
        final String jwtTokenHeaderName = getAuthHeaderName();
        final String jwtToken = doAdminLoginUtil();
        final MemberDto originMemberOne = buildTestMember();
        final MemberDto originMemberTwo = buildTestMember();
        final MemberDto originMemberThree = buildTestMember();

        final String entityIdOne = doCreateMamberOnServerTest(jwtTokenHeaderName, jwtToken, originMemberOne);
        final String entityIdTwo = doCreateMamberOnServerTest(jwtTokenHeaderName, jwtToken, originMemberTwo);
        final String entityIdThree = doCreateMamberOnServerTest(jwtTokenHeaderName, jwtToken, originMemberThree);

        mockMvc.perform(get("/members/get-all-list")
                .header(jwtTokenHeaderName, jwtToken)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", anyOf(is(entityIdOne), is(entityIdTwo), is(entityIdThree))))
                .andExpect(jsonPath("$[0].firstName", is("Dima")))
                .andExpect(jsonPath("$[0].lastName", is("Parkhomenko")))
                .andExpect(jsonPath("$[0].postalCode", is("12345")))
                .andExpect(jsonPath("$[1].id", anyOf(is(entityIdOne), is(entityIdTwo), is(entityIdThree))))
                .andExpect(jsonPath("$[1].firstName", is("Dima")))
                .andExpect(jsonPath("$[1].lastName", is("Parkhomenko")))
                .andExpect(jsonPath("$[1].postalCode", is("12345")))
                .andExpect(jsonPath("$[2].id", anyOf(is(entityIdOne), is(entityIdTwo), is(entityIdThree))))
                .andExpect(jsonPath("$[2].firstName", is("Dima")))
                .andExpect(jsonPath("$[2].lastName", is("Parkhomenko")))
                .andExpect(jsonPath("$[2].postalCode", is("12345")));
    }

    @Test
    public void admin_get_unreal_member_application_json_test() throws Exception {
        final String jwtTokenHeaderName = getAuthHeaderName();
        final String jwtToken = doAdminLoginUtil();
        final String unrealEntityId = "-1";

        mockMvc.perform(get("/members/" + unrealEntityId)
                .header(jwtTokenHeaderName, jwtToken)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(400));
    }

    private String doAdminLoginUtil() throws Exception {
        final Map<String, String> payload = buildLoginData();

        mockMvc.perform(post("/admins/sign-up")
                .content(objectMapper.writeValueAsString(payload))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200));

        MvcResult mvcResultLogin = mockMvc.perform(post("/admins/login")
                .content(objectMapper.writeValueAsString(payload))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200)).andReturn();

        String jwtToken = getTokenFromMvcResult(mvcResultLogin);
        return jwtToken;
    }

    private MemberDto buildTestMember() {
        MemberDto originMember = new MemberDto("Dima",
                "Parkhomenko",
                "12345",
                LocalDate.of(1991, 9, 24),
                null);
        return originMember;
    }

    private Map<String, String> buildLoginData() {
        HashMap<String, String> data = new HashMap<String, String>() {
            {
                put(Constants.USERNAME_LOGIN_FORM_PARAMETER_KEY, "Dmytro");
                put(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY, "qwerty");
            }
        };
        Map<String, String> payload = Collections.unmodifiableMap(data);
        return payload;
    }

    private String getAuthHeaderName() {
        return env.getProperty(Constants.JWT_TOKEN_HEADER_NAME_KEY);
    }

    private void compareMembers(MemberDto originMember, MemberDto fetchedMember) {
        Assert.assertEquals(originMember.firstName, fetchedMember.firstName);
        Assert.assertEquals(originMember.lastName, fetchedMember.lastName);
        Assert.assertEquals(originMember.birthDate, fetchedMember.birthDate);
        Assert.assertEquals(originMember.postalCode, fetchedMember.postalCode);
        Assert.assertArrayEquals(originMember.image, fetchedMember.image);
    }
    
    private String getTokenFromMvcResult(MvcResult mvcResult) {
        return mvcResult.getResponse().getHeader(getAuthHeaderName());
    }
}
