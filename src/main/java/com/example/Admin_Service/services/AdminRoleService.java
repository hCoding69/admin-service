package com.example.Admin_Service.services;

import com.example.Admin_Service.config.WebClientConfig;
import com.example.Admin_Service.dto.RoleDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

public class AdminRoleService {

    private final WebClient webClient;

    public AdminRoleService(WebClient webClient){
        this.webClient = webClient;
    }

    public Mono<List<RoleDTO>> getRoles(String token){
        return webClient.get()
                .uri("http://localhost:8082/api/roles")
                .header("Authorization", token)
                .retrieve()
                .bodyToFlux(RoleDTO.class)
                .collectList();
    }

    public Mono<List<RoleDTO>> getRolesWihPermissions(String token){
        return webClient.get()
                .uri("http://localhost:8082/api/roles/with-permissions")
                .header("Authorization", token)
                .retrieve()
                .bodyToFlux(RoleDTO.class)
                .collectList();
    }

    public Mono<List<RoleDTO>> getRoleWihPermissions(String token, Long id){
        return webClient.get()
                .uri("http://localhost:8082/api/roles/with-permissions/{id}", id)
                .header("Authorization", token)
                .retrieve()
                .bodyToFlux(RoleDTO.class)
                .collectList();
    }
}
