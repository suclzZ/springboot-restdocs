package com.sucl.restdocs.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sucl.restdocs.MockTest;
import com.sucl.restdocs.entity.User;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import javax.servlet.RequestDispatcher;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author sucl
 * @since 2019/11/5
 */
public class UserTests extends MockTest {

    @Test
    public void indexExample() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/"))
                .andExpect(status().isOk())
                .andDo(document("user",
                        links(
                                linkWithRel("notes").description("The <<resources-notes,Notes resource>>"),
                                linkWithRel("tags").description("The <<resources-tags,Tags resource>>"),
                                linkWithRel("profile").description("The ALPS profile for the service")),
                        responseFields(
                                subsectionWithPath("_links").description("<<resources-index-links,Links>> to other resources"))));

    }

    @Test
    public void saveUser() throws Exception {
        User user = User.of()
                .setUserId(1l)
                .setUserName("tom")
                .setUserCaption("汤姆")
                .setAge(22)
                .setTelephone("15112311111")
                .setEmail("tom@123.com");
        this.mockMvc.perform(
                post("/user")
                .content(new ObjectMapper().writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("saveUser"));
    }

    @Test
    public void getUser() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/user/{userId}",1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("getUser",
                        pathParameters(
                                parameterWithName("userId").description("用户id")
                        )
                ))
        ;
    }

    @Test
    public void getUsers() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.get("/user")
                .param("userName","lucy")
                .param("userCaption","露西")
                .param("age","18")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("getUsers",
                        requestParameters(
                                parameterWithName("userName").description("用户名")
                                ,parameterWithName("userCaption").description("用户姓名")
                                ,parameterWithName("age").description("年龄")
                        )
                ));
    }

    @Test
    public void deleteUser() throws Exception {
        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/user/{userId}",1)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("deleteUser",
                        pathParameters(
                                parameterWithName("userId").description("用户id")
                        )
                ));
    }

    @Test
    public void errorUser() throws Exception {
        this.mockMvc
                .perform(get("/error")
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 400)
                        .requestAttr(RequestDispatcher.ERROR_REQUEST_URI, "/user")
                        .requestAttr(RequestDispatcher.ERROR_MESSAGE,"The tag 'http://localhost:8080/xxx/' does not exist"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error", Matchers.is("Bad Request")))
                .andExpect(jsonPath("timestamp", Matchers.is(notNullValue())))
                .andExpect(jsonPath("status", Matchers.is(400)))
                .andExpect(jsonPath("path", Matchers.is(notNullValue())))
                .andDo(document("error",
                        responseFields(
                                fieldWithPath("error").description("The HTTP error that occurred, e.g. `Bad Request`"),
                                fieldWithPath("message").description("A description of the cause of the error"),
                                fieldWithPath("path").description("The path to which the request was made"),
                                fieldWithPath("status").description("The HTTP status code, e.g. `400`"),
                                fieldWithPath("timestamp").description("The time, in milliseconds, at which the error occurred"))));
    }

}
