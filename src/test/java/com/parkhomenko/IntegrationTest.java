package com.parkhomenko;

import com.parkhomenko.common.Constants;
import com.parkhomenko.member.MemberDto;
import com.parkhomenko.member.Member;
import com.parkhomenko.admin.Admin;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Base64;
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

import static org.hamcrest.Matchers.*;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@AutoConfigureMockMvc
public class IntegrationTest {

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

        MvcResult mvcResult = mockMvc.perform(post("/members")
                .content(objectMapper.writeValueAsString(member))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(401)).andReturn();

        Assert.assertEquals(mvcResult.getResponse().getContentAsString(), "{\"message\":\"Unauthorized request: invalid token\"}");
    }

    @Test
    public void unauthorized_create_application_xml_test() throws Exception {
        final MemberDto member = new MemberDto("Dima", "Parkhomenko", null, null, null);

        MvcResult mvcResult = mockMvc.perform(post("/members")
                .content(objectMapper.writeValueAsString(member))
                .contentType(MediaType.APPLICATION_XML))
                .andDo(print())
                .andExpect(status().is(401)).andReturn();

        Assert.assertEquals(mvcResult.getResponse().getContentAsString(), "<root><message>Unauthorized request: invalid token</message></root>");
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
    public void invalid_sing_up_data_application_json_test() throws Exception {
        HashMap<String, String> payloadEmptyValues = buildLoginData("", "");

        mockMvc.perform(post("/admins/sign-up")
                .content(objectMapper.writeValueAsString(payloadEmptyValues))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(400));

        HashMap<String, String> payloadNullValus = buildLoginData(null, null);

        mockMvc.perform(post("/admins/sign-up")
                .content(objectMapper.writeValueAsString(payloadNullValus))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(400));

        // sing up twise with the same login
        final Map<String, String> payload = buildLoginData();

        mockMvc.perform(post("/admins/sign-up")
                .content(objectMapper.writeValueAsString(payload))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200));

        mockMvc.perform(post("/admins/sign-up")
                .content(objectMapper.writeValueAsString(payload))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(400));
    }

    @Test
    public void invalid_login_data_application_json_test() throws Exception {
        HashMap<String, String> payloadEmptyValues = buildLoginData("", "");

        mockMvc.perform(post("/admins/login")
                .content(objectMapper.writeValueAsString(payloadEmptyValues))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(400));

        HashMap<String, String> payloadNullValus = buildLoginData(null, null);

        mockMvc.perform(post("/admins/login")
                .content(objectMapper.writeValueAsString(payloadNullValus))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(400));

        HashMap<String, String> payloadInvalidValues = buildLoginData("Invalid login", "Invalid pwd");

        mockMvc.perform(post("/admins/login")
                .content(objectMapper.writeValueAsString(payloadInvalidValues))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(400));
    }

    private HashMap<String, String> buildLoginData(final String login, final String pwd) {
        return new HashMap<String, String>() {
            {
                put(Constants.USERNAME_LOGIN_FORM_PARAMETER_KEY, login);
                put(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY, pwd);
            }
        };
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
                .andExpect(status().is(200))
                .andDo(print());

        MvcResult mvcResultAfterFirstLogin = mockMvc.perform(post("/admins/login")
                .content(objectMapper.writeValueAsString(payload))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andDo(print())
                .andReturn();

        final String jwtTokenAfterFirstLogin = getTokenFromMvcResult(mvcResultAfterFirstLogin);

        MvcResult mvcResultAfterSecondtLogin = mockMvc.perform(post("/admins/login")
                .content(objectMapper.writeValueAsString(payload))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200))
                .andDo(print())
                .andReturn();

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
        final MemberDto originMember = buildValidMember();

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
        final MemberDto originMember = buildValidMember();

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

    @Test
    public void success_login_for_admin_with_get_all_members_application_json_test() throws Exception {
        final String jwtTokenHeaderName = getAuthHeaderName();
        final String jwtToken = doAdminLoginUtil();
        final MemberDto originMemberOne = buildValidMember();
        final MemberDto originMemberTwo = buildValidMember();
        final MemberDto originMemberThree = buildValidMember();

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
    public void success_login_for_admin_with_get_all_members_application_xml_test() throws Exception {
        final String jwtTokenHeaderName = getAuthHeaderName();
        final String jwtToken = doAdminLoginUtil();
        final MemberDto originMemberOne = buildValidMember();
        final MemberDto originMemberTwo = buildValidMember();
        final MemberDto originMemberThree = buildValidMember();

        final String entityIdOne = doCreateMamberOnServerTest(jwtTokenHeaderName, jwtToken, originMemberOne);
        final String entityIdTwo = doCreateMamberOnServerTest(jwtTokenHeaderName, jwtToken, originMemberTwo);
        final String entityIdThree = doCreateMamberOnServerTest(jwtTokenHeaderName, jwtToken, originMemberThree);

        MvcResult mvcResult = mockMvc.perform(get("/members/get-all-list")
                .header(jwtTokenHeaderName, jwtToken)
                .accept(MediaType.APPLICATION_XML))
                .andDo(print())
                .andExpect(status().is(200)).andReturn();

        String expected
                = "<List>"
                + "<item><id>" + entityIdOne + "</id><firstName>Dima</firstName><lastName>Parkhomenko</lastName><postalCode>12345</postalCode><birthDate>1991-09-24</birthDate><image></image></item>"
                + "<item><id>" + entityIdTwo + "</id><firstName>Dima</firstName><lastName>Parkhomenko</lastName><postalCode>12345</postalCode><birthDate>1991-09-24</birthDate><image></image></item>"
                + "<item><id>" + entityIdThree + "</id><firstName>Dima</firstName><lastName>Parkhomenko</lastName><postalCode>12345</postalCode><birthDate>1991-09-24</birthDate><image></image></item>"
                + "</List>";

        Assert.assertEquals(expected, mvcResult.getResponse().getContentAsString());

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

    @Test
    public void delete_members_application_json_test() throws Exception {
        final String jwtTokenHeaderName = getAuthHeaderName();
        final String jwtToken = doAdminLoginUtil();
        final MemberDto originMemberOne = buildValidMember();
        final MemberDto originMemberTwo = buildValidMember();
        final MemberDto originMemberThree = buildValidMember();

        final String entityIdOne = doCreateMamberOnServerTest(jwtTokenHeaderName, jwtToken, originMemberOne);
        final String entityIdTwo = doCreateMamberOnServerTest(jwtTokenHeaderName, jwtToken, originMemberTwo);
        final String entityIdThree = doCreateMamberOnServerTest(jwtTokenHeaderName, jwtToken, originMemberThree);

        mockMvc.perform(delete("/members")
                .header(jwtTokenHeaderName, jwtToken)
                .param(Constants.LIST_ID_KEY, entityIdOne, entityIdTwo, entityIdThree)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(200));

        mockMvc.perform(get("/members/get-all-list")
                .header(jwtTokenHeaderName, jwtToken)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void double_sign_up_by_admin_application_json_test() throws Exception {
        final Map<String, String> payload = buildLoginData();

        mockMvc.perform(post("/admins/sign-up")
                .content(objectMapper.writeValueAsString(payload))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(200));

        mockMvc.perform(post("/admins/sign-up")
                .content(objectMapper.writeValueAsString(payload))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is(400));
    }

    @Test
    public void update_member_with_invalid_data_application_json_test() throws Exception {
        final String jwtTokenHeaderName = getAuthHeaderName();
        final String jwtToken = doAdminLoginUtil();

        // invalid id test
        final MemberDto memberWithInvalidId = new MemberDto("I am an invalid id", "Dima",
                "Parkhomenko", "12345", LocalDate.of(1991, Month.SEPTEMBER, 24), null);

        mockMvc.perform(put("/members")
                .header(jwtTokenHeaderName, jwtToken)
                .content(objectMapper.writeValueAsString(memberWithInvalidId))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(400));

        final MemberDto originMember = buildValidMember();
        final String entityId = doCreateMamberOnServerTest(jwtTokenHeaderName, jwtToken, originMember);

        memberServerValidation(entityId, jwtTokenHeaderName, jwtToken, put("/members"));
    }

    @Test
    public void create_member_with_invalid_data_application_json_test() throws Exception {
        final String jwtTokenHeaderName = getAuthHeaderName();
        final String jwtToken = doAdminLoginUtil();

        memberServerValidation(null, jwtTokenHeaderName, jwtToken, post("/members"));
    }

    private void memberServerValidation(String entityId,
            String jwtTokenHeaderName,
            String jwtToken, MockHttpServletRequestBuilder putReqBuilder) throws Exception {
        // invalid first name test: first name equals NULL
        final MemberDto memberWithInvalidFirstNameNull = new MemberDto(entityId, null,
                "Parkhomenko", "12345", LocalDate.of(1991, Month.SEPTEMBER, 24), null);

        putReqBuilder
                .header(jwtTokenHeaderName, jwtToken); // add header only once for all requests below
        
        mockMvc.perform(putReqBuilder
                .content(objectMapper.writeValueAsString(memberWithInvalidFirstNameNull))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(400));

        // invalid first name test: first name equals EMPTY STRING
        final MemberDto memberWithInvalidFirstNameEmpty = new MemberDto(entityId, "",
                "Parkhomenko", "12345", LocalDate.of(1991, Month.SEPTEMBER, 24), null);

        mockMvc.perform(putReqBuilder
                .content(objectMapper.writeValueAsString(memberWithInvalidFirstNameEmpty))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(400));

        // invalid second name test: second name equals NULL
        final MemberDto memberWithInvalidSecondNameNull = new MemberDto(entityId, "Dima",
                null, "12345", LocalDate.of(1991, Month.SEPTEMBER, 24), null);

        mockMvc.perform(putReqBuilder
                .content(objectMapper.writeValueAsString(memberWithInvalidSecondNameNull))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(400));

        // invalid second name test: second name equals EMPTY STRING
        final MemberDto memberWithInvalidSecondNameEmpty = new MemberDto(entityId, "Dima",
                "", "12345", LocalDate.of(1991, Month.SEPTEMBER, 24), null);

        mockMvc.perform(putReqBuilder
                .content(objectMapper.writeValueAsString(memberWithInvalidSecondNameEmpty))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(400));

        // invalid ZIP test
        final MemberDto memberWithInvalidZip = new MemberDto(entityId, "Dima",
                "Parkhomenko", "I am an invalid ZIP", LocalDate.of(1991, Month.SEPTEMBER, 24), null);

        mockMvc.perform(putReqBuilder
                .content(objectMapper.writeValueAsString(memberWithInvalidZip))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(400));

        // invalid ZIP test: ZIP equals NULL
        final MemberDto memberWithZipNull = new MemberDto(entityId, "Dima",
                "Parkhomenko", null, LocalDate.of(1991, Month.SEPTEMBER, 24), null);

        mockMvc.perform(putReqBuilder
                .content(objectMapper.writeValueAsString(memberWithZipNull))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(400));

        // invalid ZIP test: ZIP equals EMPTY STRING
        final MemberDto memberWithZipEmpty = new MemberDto(entityId, "Dima",
                "Parkhomenko", "", LocalDate.of(1991, Month.SEPTEMBER, 24), null);

        mockMvc.perform(putReqBuilder
                .content(objectMapper.writeValueAsString(memberWithZipEmpty))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(400));

        // invalid birth day test: birth day equals NULL
        final MemberDto memberWithBirthDayNull = new MemberDto(entityId, "Dima",
                "Parkhomenko", "123456", null, null);

        mockMvc.perform(putReqBuilder
                .content(objectMapper.writeValueAsString(memberWithBirthDayNull))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(400));

        // invalid birth day test: birth day is after now date
        final MemberDto memberWithBirthDayAfterNowDate = new MemberDto(entityId, "Dima",
                "Parkhomenko", "123456", LocalDate.now().plusDays(1), null);

        mockMvc.perform(putReqBuilder
                .content(objectMapper.writeValueAsString(memberWithBirthDayAfterNowDate))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(400));

        // invalid birth day test: birth day is not after LocalDate.MIN from Java JDK 1.8
        final MemberDto memberWithBirthDayNotAfterMinDate = new MemberDto(entityId, "Dima",
                "Parkhomenko", "123456", LocalDate.MIN, null);

        mockMvc.perform(putReqBuilder
                .content(objectMapper.writeValueAsString(memberWithBirthDayNotAfterMinDate))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andExpect(status().is(400));
        
        // invalid image size: more then 1 Mb
        String bigImageBase64 = getTestImageBase64("space-x.jpg");
        final MemberDto memberWithBigImage = new MemberDto(entityId, "Dima",
                "Parkhomenko", "123456", LocalDate.of(1991, Month.SEPTEMBER, 24), bigImageBase64);

        mockMvc.perform(putReqBuilder
                .content(objectMapper.writeValueAsString(memberWithBigImage))
                .contentType(MediaType.APPLICATION_JSON_UTF8))
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

    private MemberDto buildValidMember() throws IOException {
        MemberDto originMember = new MemberDto("Dima",
                "Parkhomenko",
                "12345",
                LocalDate.of(1991, Month.SEPTEMBER, 24),
                "");
        return originMember;
    }

    private MemberDto buildValidMemberWithImage() throws IOException {
        String testImageBase64 = getTestImageBase64("gta-vs.jpg");

        MemberDto originMember = new MemberDto("Dima",
                "Parkhomenko",
                "12345",
                LocalDate.of(1991, Month.SEPTEMBER, 24),
                testImageBase64);
        return originMember;
    }

    private Map<String, String> buildLoginData() {
        HashMap<String, String> data = buildLoginData("Dmytro", "qwerty");
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
        Assert.assertEquals(originMember.image, fetchedMember.image);
    }

    private String getTokenFromMvcResult(MvcResult mvcResult) {
        return mvcResult.getResponse().getHeader(getAuthHeaderName());
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
    
    private String getTestImageBase64(String imageName) throws IOException {
        InputStream image = getClass().getClassLoader().getResourceAsStream(imageName);
        
        byte[] targetArray = new byte[image.available()];
        image.read(targetArray);
        
        String base64Image = Base64.getEncoder().encodeToString(targetArray);
        return base64Image;
    }
}
