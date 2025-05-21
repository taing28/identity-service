package com.example.demo.service;

import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService _userService;

    @MockitoBean
    private UserRepository _userRepository;

    private UserCreationRequest request;
    private UserResponse userResponse;
    private User user;
    private LocalDate dob;

    @BeforeEach
    void initData() {
        dob = LocalDate.of(2004, 10, 2);

        request = UserCreationRequest.builder()
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .password("Matkhau123@")
                .dob(dob)
                .build();

        userResponse = UserResponse.builder()
                .id("acf0600f538b3")
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .dob(dob)
                .build();

        user = User.builder()
                .id("acf0600f538b3")
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .dob(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success() {
        // GIVEN
        when(_userRepository.existsByUsernameIgnoreCase(anyString()))
                .thenReturn(false);
        when(_userRepository.save(any())).thenReturn(user);

        // WHEN
        var response = _userService.createRequest(request);

        // THEN
        Assertions.assertThat(response.getId()).isEqualTo("acf0600f538b3");
        Assertions.assertThat(response.getUsername()).isEqualTo("john");
    }

    @Test
    void createUser_userExisted_fail() {
        // GIVEN
        when(_userRepository.existsByUsernameIgnoreCase(anyString()))
                .thenReturn(true);

        // WHEN
        var exception = assertThrows(AppException.class,
                () -> _userService.createRequest(request));

        Assertions.assertThat(exception.getErrorCode().getCode())
                .isEqualTo(1002);
    }
}
