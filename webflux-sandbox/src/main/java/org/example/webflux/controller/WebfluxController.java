package org.example.webflux.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class WebfluxController {

    @GetMapping("/test")
    public Mono<String> test()  {
        return Mono.just("Test Data webflux");
    }
}
