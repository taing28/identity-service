package com.example.demo.service.Iservice;

import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.request.UserUpdateRequest;
import com.example.demo.dto.response.UserResponse;

import java.util.List;

public interface IUserService {
    List<UserResponse> getUsers();
    UserResponse getUser(String userId);
    UserResponse getMyInfo();
    UserResponse createUser(UserCreationRequest request);
    UserResponse updateUser(String userId, UserUpdateRequest request);
    UserResponse removeUser(String userId);
}
