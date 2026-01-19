package com.example.Admin_Service.services;

import com.example.Admin_Service.config.WebClientConfig;
import com.example.Admin_Service.dto.PermissionDTO;
import com.example.Admin_Service.dto.RoleDTO;
import com.example.Admin_Service.dto.RoleWithPermissionsDTO;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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


    public Mono<List<RoleWithPermissionsDTO>> getRolesWithPermissions() {
        return webClient.get()
                .uri("http://localhost:8082/api/roles/with-permissions")
                .retrieve()
                // Gestion des erreurs HTTP
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .doOnNext(body ->
                                        System.err.println("Erreur WebClient: " + body))
                                .flatMap(body -> Mono.error(
                                        new RuntimeException("Erreur HTTP " + clientResponse.statusCode() + ": " + body))))
                .bodyToFlux(RoleWithPermissionsDTO.class)
                .collectList()
                // Gestion des exceptions dans la chaîne réactive
                .doOnError(e -> System.err.println("Exception lors du getRolesWithPermissions: " + e.getMessage()));
    }






    public Mono<RoleWithPermissionsDTO> getRoleWithPermissions(Long id) {
        return webClient.get()
                .uri("http://localhost:8082/api/roles/with-permissions/{id}", id)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new RuntimeException("ERROR FROM AUTHSERVICE: " + body))
                )
                .bodyToMono(RoleWithPermissionsDTO.class);
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

    public Mono<String> addPermissionToRole(List<PermissionDTO> permissions, Long roleId) {
        return webClient.post()
                .uri("http://localhost:8082/api/roles/{roleId}/permissions", roleId)
                .bodyValue(permissions)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, resp ->
                        resp.bodyToMono(String.class)
                                .flatMap(msg -> Mono.error(new RuntimeException(msg)))
                )
                .bodyToMono(String.class);
    }

    public Mono<String> removePermissionFromRole(Long roleId, Long permissionId){
        return webClient.delete()
                .uri("http://localhost:8082/api/roles/{roleId}/permissions/{permissionId}", roleId, permissionId)
                .retrieve()
                .bodyToMono(String.class);
    }

}
