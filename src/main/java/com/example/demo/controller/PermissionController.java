package com.example.demo.controller;

import com.example.demo.dto.request.PermissionRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.PermissionResponse;
import com.example.demo.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService _permissionService;

    @GetMapping("")
    ApiResponse<List<PermissionResponse>> getAll() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(_permissionService.getAll())
                .build();
    }

    @PostMapping("")
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .result(_permissionService.create(request))
                .build();
    }

    @DeleteMapping("/{permission}")
    ApiResponse<Void> delete(@PathVariable String permission) {
        _permissionService.delete(permission);
        return ApiResponse.<Void>builder().build();
    }
}
