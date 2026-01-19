package com.example.Admin_Service.controllers;


import com.example.Admin_Service.dto.PermissionDTO;
import com.example.Admin_Service.dto.RoleDTO;
import com.example.Admin_Service.dto.RoleWithPermissionsDTO;
import com.example.Admin_Service.services.AdminRoleService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.List;



@RestController
@RequestMapping("/api/admin/roles")
public class AdminRoleController {

    private final AdminRoleService adminRoleService;

    public AdminRoleController(AdminRoleService adminRoleService) {
        this.adminRoleService = adminRoleService;
    }

    @GetMapping
    public Mono<List<RoleDTO>> getRoles(HttpServletRequest request) {


        return adminRoleService.getRoles();
    }

    @GetMapping("/with-permissions")
    public Mono<ResponseEntity<List<RoleWithPermissionsDTO>>> getRolesWithPermissions() {
        return adminRoleService.getRolesWithPermissions()
                .map(roles -> ResponseEntity.ok(roles))
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }


    @GetMapping("/with-permissions/{id}")
    public Mono<ResponseEntity<RoleWithPermissionsDTO>> getRoleWithPermissions(

            @PathVariable Long id) {

        return adminRoleService.getRoleWithPermissions(id)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    public Mono<ResponseEntity<RoleDTO>> createRole(
            @RequestBody RoleDTO roleDTO) {

        return adminRoleService.createRole(roleDTO)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<RoleDTO>> updateRole(
            @PathVariable Long id,
            @RequestBody RoleDTO roleDTO) {

        return adminRoleService.updateRole(id, roleDTO)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteRole(
            @PathVariable Long id) {

        return adminRoleService.delete(id)
                .thenReturn(ResponseEntity.ok().build());
    }

    // Ajouter une ou plusieurs permissions à un rôle
    @PostMapping("/{roleId}/permissions")
    public Mono<ResponseEntity<String>> addPermissionsToRole(
            @PathVariable Long roleId,
            @RequestBody List<PermissionDTO> permissions) {

        return adminRoleService.addPermissionToRole(permissions, roleId)
                .map(msg -> ResponseEntity.ok(msg))
                .onErrorResume(e -> Mono.just(
                        ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(e.getMessage())
                ));
    }
    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    public Mono<ResponseEntity<String>> removePermissionFromRole(@PathVariable Long roleId,
                                                                 @PathVariable Long permissionId) {
        return adminRoleService.removePermissionFromRole(roleId, permissionId)
                .map(ResponseEntity::ok);
    }
}
