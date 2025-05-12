package com.example.demo.service;

import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.request.UserUpdateRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.Iservice.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements IUserService {
    UserRepository _userRepository;
    UserMapper _userMapper;

    @Override
    public User createRequest(UserCreationRequest request) {
        if (_userRepository.existsByUsernameIgnoreCase(request.getUsername().trim())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = _userMapper.toUser(request);

        return _userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        return _userRepository.findAll();
    }

    @Override
    public UserResponse getUser(String userId) {
        return _userMapper.toUserResponse(_userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    @Override
    public User removeUser(String userId) {
        User user = _userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        _userRepository.deleteById(userId);
        return user;
    }

    @Override
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = _userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        _userMapper.updateUser(user, request);

        return _userMapper.toUserResponse(_userRepository.save(user));
    }
}
