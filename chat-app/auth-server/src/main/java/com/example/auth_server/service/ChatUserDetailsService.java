package com.example.auth_server.service;

import com.example.auth_server.DTO.InternalUserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class ChatUserDetailsService implements UserDetailsService {

    private final WebClient webClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Loading user by username: {}", username);


        InternalUserResponse user = webClient.get()
                .uri("http://localhost:9400/internal/users/by-username/{username}", username)
                .retrieve()
                .bodyToMono(InternalUserResponse.class)
                .block();

        if(user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return User.builder()
                .username(user.username())
                .password(user.passwordHash())
                .roles(user.roles().toArray(new String[0]))
                .build();
    }

    public UUID fetchUserIdFromUserService(String username) {
        InternalUserResponse user = webClient.get()
                .uri("http://localhost:9400/internal/users/by-username/{username}", username)
                .retrieve()
                .bodyToMono(InternalUserResponse.class)
                .block();

        return user.id();
    }
}
