package com.example.Admin_Service.controllers;

import com.example.Admin_Service.dto.PermissionDTO;
import com.example.Admin_Service.services.PermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/admin/permissions")
public class AdminPermissionController {

    private final PermissionService permissionService;

    public AdminPermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping
    public Mono<ResponseEntity<List<PermissionDTO>>> getPermissions() {

        return permissionService.getPermissions()
                .map(ResponseEntity::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<PermissionDTO>> createPermission(
            @RequestBody PermissionDTO request) {

        return permissionService.createPermission(request)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<PermissionDTO>> updatePermission(
            @PathVariable Long id,
            @RequestBody PermissionDTO request) {

        return permissionService.editPermission(id, request)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deletePermission(
            @PathVariable Long id) {

        return permissionService.deletePermission(id)
                .thenReturn(ResponseEntity.ok().build());
    }
}
