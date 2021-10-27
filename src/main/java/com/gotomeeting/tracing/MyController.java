package com.gotomeeting.tracing;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class MyController {

    private final WebClient client;

    @GetMapping("{no}")
    public Mono<String> get(@PathVariable int no) {
        log.info("Called with {}", no);
        return no < 10 ? callDown(no + 1) : Mono.just("Hi");
    }

    private Mono<String> callDown(int no) {
        return client.get().uri("/" + no).accept(MediaType.TEXT_PLAIN).retrieve().bodyToMono(String.class)
                .then(client.get().uri("/other").accept(MediaType.TEXT_PLAIN).retrieve().bodyToMono(String.class));
    }

    @GetMapping("other")
    public String other() {
        return "Other";
    }
}
