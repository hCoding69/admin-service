package com.example.Admin_Service.controllers;

import com.example.Admin_Service.dto.RoleDTO;
import com.example.Admin_Service.services.AdminUserRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserRoleController {

    private final AdminUserRoleService adminUserRoleService;

    public AdminUserRoleController(AdminUserRoleService adminUserRoleService) {
        this.adminUserRoleService = adminUserRoleService;
    }

    @GetMapping("/{id}/roles")
    public Mono<ResponseEntity<List<RoleDTO>>> getUserRoles(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {

        return adminUserRoleService.getUserRolesWithPermissions(token, id)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/{id}/roles")
    public Mono<ResponseEntity<Void>> addRolesToUser(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestBody Set<Long> roleIds) {

        return adminUserRoleService.addRolesToUser(token, id, roleIds)
                .thenReturn(ResponseEntity.ok().build());
    }

    @DeleteMapping("/{id}/roles")
    public Mono<ResponseEntity<Void>> removeRolesFromUser(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestBody Set<Long> roleIds) {

        return adminUserRoleService.removeRolesFromUser(token, id, roleIds)
                .thenReturn(ResponseEntity.ok().build());
    }
}
