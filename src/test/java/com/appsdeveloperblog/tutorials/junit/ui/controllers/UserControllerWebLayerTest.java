package com.appsdeveloperblog.tutorials.junit.ui.controllers;

import com.appsdeveloperblog.tutorials.junit.service.UsersService;
import com.appsdeveloperblog.tutorials.junit.service.UsersServiceImpl;
import com.appsdeveloperblog.tutorials.junit.shared.UserDto;
import com.appsdeveloperblog.tutorials.junit.ui.request.UserDetailsRequestModel;
import com.appsdeveloperblog.tutorials.junit.ui.response.UserRest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = UsersController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class})
//@AutoConfigureMockMvc(addFilters = false)
public class UserControllerWebLayerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UsersService usersService;

    @Test
    @DisplayName("User can be created")
    void testCreateUser_whenValidUserDetailsProvided_returnCreatedUserDetails() throws Exception {
        // Arrange
        UserDetailsRequestModel user = new UserDetailsRequestModel();
        user.setFirstName("Duong");
        user.setLastName("Pham Hoang");
        user.setEmail("duongph.4a@gmail.com");
        user.setPassword("123456a@");
        user.setRepeatPassword("123456a@");

        UserDto userDto = new ModelMapper().map(user, UserDto.class);
        userDto.setUserId(UUID.randomUUID().toString());
        when(usersService.createUser(any(UserDto.class))).thenReturn(userDto);

        RequestBuilder request = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user));
        // Act
        MvcResult mvcResult = mockMvc.perform(request).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();

        UserRest userRest = new ObjectMapper().readValue(contentAsString, UserRest.class);
//        Dòng trên sẽ convert chuỗi string sang kiểu dữ liệu mà ta cần

        // Assert
        Assertions.assertEquals(user.getFirstName(),
                userRest.getFirstName(),
                "The returned user first name is most likely incorrect");

        Assertions.assertEquals(user.getLastName(),
                userRest.getLastName(),
                "The returned user last name is most likely incorrect");

        Assertions.assertEquals(user.getEmail(),
                userRest.getEmail(),
                "The returned user email is most likely incorrect");

        Assertions.assertFalse(userRest.getUserId().isEmpty(),
                "userId should not be empty");
    }

}
