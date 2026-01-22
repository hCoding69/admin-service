package com.example.Admin_Service.controllers;


import com.example.Admin_Service.dto.PermissionDTO;
import com.example.Admin_Service.dto.RoleDTO;
import com.example.Admin_Service.dto.RoleWithPermissionsDTO;
import com.example.Admin_Service.services.AdminRoleService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    // 🔹 Récupérer la liste des rôles
    // Les admins ou super admins peuvent voir
    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN') or hasAuthority('MANAGE_ROLES')")
    @GetMapping
    public Mono<List<RoleDTO>> getRoles(){
        return adminRoleService.getRoles();
    }

    // 🔹 Récupérer la liste des rôles avec leurs permissions
    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN') or hasAuthority('MANAGE_ROLES') or hasAuthority('MANAGE_PERMISSIONS')")
    @GetMapping("/with-permissions")
    public Mono<ResponseEntity<List<RoleWithPermissionsDTO>>> getRolesWithPermissions() {
        return adminRoleService.getRolesWithPermissions()
                .map(roles -> ResponseEntity.ok(roles))
                .defaultIfEmpty(ResponseEntity.noContent().build());
    }

    // 🔹 Récupérer un rôle avec ses permissions
    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN') or hasAuthority('MANAGE_ROLES') or hasAuthority('MANAGE_PERMISSIONS')")
    @GetMapping("/with-permissions/{id}")
    public Mono<ResponseEntity<RoleWithPermissionsDTO>> getRoleWithPermissions(
            @PathVariable Long id) {
        return adminRoleService.getRoleWithPermissions(id)
                .map(ResponseEntity::ok);
    }

    // 🔹 Créer un rôle
    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN') and hasAuthority('MANAGE_ROLES')")
    @PostMapping
    public Mono<ResponseEntity<RoleDTO>> createRole(@RequestBody RoleDTO roleDTO) {
        return adminRoleService.createRole(roleDTO)
                .map(ResponseEntity::ok);
    }

    // 🔹 Modifier un rôle
    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN') and hasAuthority('MANAGE_ROLES')")
    @PutMapping("/{id}")
    public Mono<ResponseEntity<RoleDTO>> updateRole(
            @PathVariable Long id,
            @RequestBody RoleDTO roleDTO) {
        return adminRoleService.updateRole(id, roleDTO)
                .map(ResponseEntity::ok);
    }

    // 🔹 Supprimer un rôle
    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN') and hasAuthority('MANAGE_ROLES')")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteRole(@PathVariable Long id) {
        return adminRoleService.delete(id)
                .thenReturn(ResponseEntity.ok().build());
    }

    // 🔹 Ajouter des permissions à un rôle
    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN') and hasAuthority('MANAGE_PERMISSIONS')")
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

    // 🔹 Retirer une permission d'un rôle
    @PreAuthorize("hasRole('ROLE_SYSTEM_ADMIN') and hasAuthority('MANAGE_PERMISSIONS')")
    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    public Mono<ResponseEntity<String>> removePermissionFromRole(
            @PathVariable Long roleId,
            @PathVariable Long permissionId) {
        return adminRoleService.removePermissionFromRole(roleId, permissionId)
                .map(ResponseEntity::ok);
    }
}

