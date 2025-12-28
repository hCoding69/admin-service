package com.example.Admin_Service.controllers;


import com.example.Admin_Service.dto.PermissionDTO;
import com.example.Admin_Service.services.PermissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/admin/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService){
        this.permissionService = permissionService;
    }

    @PreAuthorize("hasAuthority('ADMIN:PERMISSION:VIEW')")
    @GetMapping
    public Mono<List<PermissionDTO>> getAll(@RequestHeader("Authorization") String token){
        return permissionService.getPermissions(token);
    }

    @PreAuthorize("hasAuthority('ADMIN:PERMISSION:CREATE')")
    @PostMapping
    public Mono<PermissionDTO> create(
            @RequestHeader("Authorization") String token,
            @RequestBody PermissionDTO dto) {
        return permissionService.createPermission(token, dto);
    }

    @PreAuthorize("hasAuthority('ADMIN:PERMISSION:UPDATE')")
    @PutMapping("/{id}")
    public Mono<PermissionDTO> update(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestBody PermissionDTO req
            ) {
        return permissionService.editPermission(token, id, req);
    }
    @PreAuthorize("hasAuthority('ADMIN:PERMISSION:DELETE')")
    @DeleteMapping("/{id}")
    public Mono<Void> delete(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        return permissionService.deletePermission(token, id);
    }
}
