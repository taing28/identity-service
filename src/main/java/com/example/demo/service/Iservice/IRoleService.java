package com.example.demo.service.Iservice;

import com.example.demo.dto.request.RoleRequest;
import com.example.demo.dto.response.RoleResponse;

import java.util.List;

public interface IRoleService {
    List<RoleResponse> getAll();
    RoleResponse create(RoleRequest request);
    void delete(String role);
}
