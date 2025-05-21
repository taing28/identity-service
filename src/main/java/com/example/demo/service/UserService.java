package com.example.demo.service;

import com.example.demo.dto.request.UserCreationRequest;
import com.example.demo.dto.request.UserUpdateRequest;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.Iservice.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService implements IUserService {
    UserRepository _userRepository;
    UserMapper _userMapper;
    PasswordEncoder _passwordEncoder;
    RoleRepository _roleRepository;

    @Override
    public UserResponse createRequest(UserCreationRequest request) {
        if (_userRepository.existsByUsernameIgnoreCase(request.getUsername().trim())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = _userMapper.toUser(request);
        user.setPassword(_passwordEncoder.encode(request.getPassword()));

        // Set default role for user
        Set<Role> roles = new HashSet<>();
        Role defaultRole = _roleRepository.findById("USER").orElseThrow(() -> new AppException(ErrorCode.DEFAULT_ROLE_NOT_EXISTED));
        roles.add(defaultRole);
        user.setRoles(roles);
        return _userMapper.toUserResponse(_userRepository.save(user));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
//    @PreAuthorize("hasAuthority('APPROVE_POST')") authorize by permission
    public List<UserResponse> getUsers() {
        log.info("In method get Users");
        return _userMapper.toUserResponses(_userRepository.findAll());
    }

    @Override
    @PostAuthorize("returnObject.username == authentication.name")
    // User can only see result if result's username == user's username
    public UserResponse getUser(String userId) {
        log.info("In method get User by Id");
        return _userMapper.toUserResponse(_userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    @Override
    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        User user = _userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return _userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse removeUser(String userId) {
        User user = _userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        _userRepository.deleteById(userId);
        return _userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = _userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        _userMapper.updateUser(user, request);
        user.setPassword(_passwordEncoder.encode(request.getPassword()));

        var roles = _roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));

        return _userMapper.toUserResponse(_userRepository.save(user));
    }
}
