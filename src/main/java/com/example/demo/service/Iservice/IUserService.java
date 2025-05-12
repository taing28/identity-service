package com.example.demo.service.Iservice;

import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import java.util.List;

public interface IUserService {
    User createRequest(UserCreationRequest request);
    List<User> getUsers();
    User getUser(String userId);
    User removeUser(String userId);
}
