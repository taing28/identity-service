package com.example.demo.service;

import com.example.demo.dto.request.PermissionRequest;
import com.example.demo.dto.response.PermissionResponse;
import com.example.demo.entity.Permission;
import com.example.demo.mapper.PermissionMapper;
import com.example.demo.repository.PermissionRepository;
import com.example.demo.service.Iservice.IPermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionService implements IPermissionService {
    PermissionRepository _permissionRepo;
    PermissionMapper _permissionMapper;
    @Override
    public PermissionResponse create(PermissionRequest request) {
        Permission permission = _permissionMapper.toPermission(request);
        return _permissionMapper.toPermissionResponse(_permissionRepo.save(permission));
    }

    @Override
    public List<PermissionResponse> getAll() {
        var permissions = _permissionRepo.findAll();
        return permissions.stream().map(_permissionMapper::toPermissionResponse).toList();
    }

    @Override
    public void delete(String permission) {
        _permissionRepo.deleteById(permission);
    }
}
