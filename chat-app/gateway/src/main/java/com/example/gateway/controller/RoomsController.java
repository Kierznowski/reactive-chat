package com.example.gateway.controller;

import com.example.gateway.DTO.CreateRoomRequest;
import com.example.gateway.DTO.RoomDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/gateway/rooms")
public class RoomsController {

    private final WebClient webClient;

    public RoomsController(@Qualifier("userWebClient") WebClient userServiceWebClient) {
        this.webClient = userServiceWebClient;
    }

    @GetMapping
    public Flux<RoomDTO> getUserRooms(@AuthenticationPrincipal OAuth2User user,
                                      @RegisteredOAuth2AuthorizedClient("chat_auth_server") OAuth2AuthorizedClient client) {
        String userId = user.getAttribute("sub");
        String token = client.getAccessToken().getTokenValue();

        return webClient.get()
                .uri("http://localhost:9300/rooms/by-user/{userId}", userId)
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .bodyToFlux(RoomDTO.class);
    }

    @PostMapping("/{roomName}")
    public Mono<RoomDTO> createRoom(@AuthenticationPrincipal OAuth2User user,
                                    @RegisteredOAuth2AuthorizedClient("chat_auth_server") OAuth2AuthorizedClient client,
                                    @PathVariable("roomName") String roomName) {
        CreateRoomRequest request = new CreateRoomRequest(roomName, user.getAttribute("sub"));

        System.out.println("ATTRIBUTES: " + user.getAttributes());
        return webClient.post()
                .uri("http://localhost:9300/rooms")
                .headers(h -> h.setBearerAuth(client.getAccessToken().getTokenValue()))
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, r ->
                        r.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new RuntimeException("Create room error: " + body)))
                )
                .bodyToMono(RoomDTO.class)
                .doOnNext(room -> System.out.println(room.name()));
    }
}
