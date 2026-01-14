package com.example.Admin_Service.services;


import com.example.Admin_Service.dto.PermissionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class PermissionService {

    private final WebClient webClient;

    public PermissionService(WebClient webClient){
        this.webClient = webClient;
    }

    public Mono<List<PermissionDTO>> getPermissions(){
        return webClient.get()
                .uri("http://localhost:8082/api/permissions")
                .retrieve()
                .bodyToFlux(PermissionDTO.class)
                .collectList();

    }

    public Mono<PermissionDTO> createPermission(String token, PermissionDTO req){
        return webClient.post()
                .uri("http://localhost:8082/api/permissions")
                .header("Authorization", "Bearer " + token)
                .bodyValue(req)
                .retrieve()
                .bodyToMono(PermissionDTO.class);
    }

    public Mono<PermissionDTO> editPermission(String token, Long id, PermissionDTO req){
        return webClient.put()
                .uri("http://localhost:8082/api/permissions/{id}", id)
                .header("Authorization", "Bearer " + token)
                .bodyValue(req)
                .retrieve()
                .bodyToMono(PermissionDTO.class);
    }

    public Mono<Void> deletePermission(String token, Long id) {
        return webClient.delete()
                .uri("http://localhost:8082/api/permissions/{id}", id)
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(Void.class);
    }


}
