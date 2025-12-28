package com.example.Admin_Service.controllers;


import com.example.Admin_Service.dto.RoleDTO;
import com.example.Admin_Service.services.AdminRoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminRoleController {
    private final AdminRoleService adminRoleService;
    public AdminRoleController(AdminRoleService adminRoleService){
        this.adminRoleService = adminRoleService;
    }

    @GetMapping("/roles")
    public Mono<List<RoleDTO>> getRoles(String token){
        return adminRoleService.getRoles(token);
    }



}
