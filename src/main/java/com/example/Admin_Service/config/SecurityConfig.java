package com.example.Admin_Service.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                // ❌ pas de CSRF (API)
                .csrf(csrf -> csrf.disable())

                // ❌ pas de session
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // ❌ pas de form login / basic auth
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                // 🔐 Sécurité des endpoints
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )

                // ❌ pas de CORS ici
                .cors(cors -> cors.disable())

                .build();
    }
}
