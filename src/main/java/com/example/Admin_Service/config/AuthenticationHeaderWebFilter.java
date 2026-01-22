package com.example.Admin_Service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class AuthenticationHeaderWebFilter implements WebFilter {

    private static final Logger log =
            LoggerFactory.getLogger(AuthenticationHeaderWebFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        var headers = exchange.getRequest().getHeaders();

        String email = headers.getFirst("X-USER-EMAIL");
        String roles = headers.getFirst("X-USER-ROLES");
        String authorities = headers.getFirst("X-USER-AUTHORITIES");

        log.info("🔐 Auth headers received:");
        log.info("  email = {}", email);
        log.info("  roles = {}", roles);
        log.info("  authorities = {}", authorities);

        if (email == null || roles == null || authorities == null) {
            log.warn("❌ Missing auth headers → anonymous");
            return chain.filter(exchange);
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        Arrays.stream(authorities.replaceAll("[\\[\\]\\s]", "").split(","))
                .filter(s -> !s.isBlank())
                .map(SimpleGrantedAuthority::new)
                .forEach(grantedAuthorities::add);

        Arrays.stream(roles.replaceAll("[\\[\\]\\s]", "").split(","))
                .filter(s -> !s.isBlank())
                .map(SimpleGrantedAuthority::new)
                .forEach(grantedAuthorities::add);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        grantedAuthorities
                );

        log.info("✅ Authentication built with authorities: {}",
                grantedAuthorities);

        return chain.filter(exchange)
                .contextWrite(
                        ReactiveSecurityContextHolder
                                .withAuthentication(authentication)
                );
    }
}
