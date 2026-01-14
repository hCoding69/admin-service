package com.example.Admin_Service.services;

import com.example.Admin_Service.dto.RoleDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
@Service
public class AdminUserRoleService {

    private final WebClient webClient;

    public AdminUserRoleService(WebClient webClient) {
        this.webClient = webClient;
    }
    public Mono<Void> addRolesToUser(String token, Long userId, Set<Long> roleIds) {
        return webClient.post()
                .uri("http://localhost:8082/api/users/{id}/roles", userId)
                .header("Authorization", token)
                .bodyValue(roleIds)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<Void> removeRolesFromUser(String token, Long userId, Set<Long> roleIds) {
        return webClient.method(org.springframework.http.HttpMethod.DELETE)
                .uri("http://localhost:8082/api/users/{id}/roles", userId)
                .header("Authorization", token)
                .bodyValue(roleIds)
                .retrieve()
                .bodyToMono(Void.class);
    }

    public Mono<List<RoleDTO>> getUserRolesWithPermissions(String token, Long userId) {
        return webClient.get()
                .uri("http://localhost:8082/api/users/{id}/roles", userId)
                .header("Authorization", token)
                .retrieve()
                .bodyToFlux(RoleDTO.class)
                .collectList();
    }

}
