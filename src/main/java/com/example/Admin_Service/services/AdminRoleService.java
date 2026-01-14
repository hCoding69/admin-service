package com.example.Admin_Service.services;

import com.example.Admin_Service.config.WebClientConfig;
import com.example.Admin_Service.dto.RoleDTO;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
@Service
public class AdminRoleService {

    private final WebClient webClient;

    public AdminRoleService(WebClient webClient){
        this.webClient = webClient;
    }

    public Mono<List<RoleDTO>> getRoles() {

        return webClient.get()
                .uri("http://localhost:8082/api/roles")
                .retrieve()
                .bodyToFlux(RoleDTO.class)
                .collectList();
    }


    public Mono<Object> getRolesWithPermissions() {
        return webClient.get()
                .uri("http://localhost:8082/api/roles/with-permissions")
                .retrieve()
                .bodyToMono(Object.class);
    }



    public Mono<RoleDTO> getRoleWithPermissions(Long id) {
        return webClient.get()
                .uri("http://localhost:8082/api/roles/with-permissions/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new RuntimeException("ERROR FROM AUTHSERVICE: " + body))
                )
                .bodyToMono(RoleDTO.class);
    }


    public Mono<RoleDTO> updateRole(Long id, RoleDTO roleDTO) {
        return webClient.put()
                .uri("http://localhost:8082/api/roles/{id}", id)
                .bodyValue(roleDTO)
                .retrieve()
                .bodyToMono(RoleDTO.class);
    }


    public Mono<RoleDTO> createRole( RoleDTO roleDTO) {
        return webClient.post()
                .uri("http://localhost:8082/api/roles")
                .bodyValue(roleDTO)
                .retrieve()
                .bodyToMono(RoleDTO.class);
    }


    public Mono<Void> delete(Long id) {
        return webClient.delete()
                .uri("http://localhost:8082/api/roles/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }

}
