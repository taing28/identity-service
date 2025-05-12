package com.example.demo.service;

import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.Iservice.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {
    @Autowired
    UserRepository _userRepository;

    @Override
    public User createRequest(UserCreationRequest request) {
        User user = new User();

        if (_userRepository.existsByUsernameIgnoreCase(request.getUsername().trim())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());

        return _userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        return _userRepository.findAll();
    }

    @Override
    public User getUser(String userId) {
        return _userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User removeUser(String userId) {
        User user = _userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        _userRepository.deleteById(userId);
        return user;
    }
}
