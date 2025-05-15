package com.example.demo.service;

import com.example.demo.dto.request.RoleRequest;
import com.example.demo.dto.response.RoleResponse;
import com.example.demo.entity.Role;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.repository.PermissionRepository;
import com.example.demo.repository.RoleRepository;
import com.example.demo.service.Iservice.IRoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleService implements IRoleService {
    RoleRepository _roleRepository;
    PermissionRepository _permissionRepository;
    RoleMapper _roleMapper;

    @Override
    public List<RoleResponse> getAll() {
        return _roleRepository.findAll()
                .stream()
                .map(_roleMapper::toRoleResponse)
                .toList();
    }

    @Override
    public RoleResponse create(RoleRequest request) {
        Role role = _roleMapper.toRole(request);
        var permissions = _permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));
        return _roleMapper.toRoleResponse(_roleRepository.save(role));
    }

    @Override
    public void delete(String role) {
        _roleRepository.deleteById(role);
    }
}
