package com.example.demo.service.Iservice;

import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.request.UserUpdateRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import java.util.List;

public interface IUserService {
    List<UserResponse> getUsers();
    UserResponse getUser(String userId);
    UserResponse getMyInfo();
    UserResponse createRequest(UserCreationRequest request);
    UserResponse updateUser(String userId, UserUpdateRequest request);
    UserResponse removeUser(String userId);
}
