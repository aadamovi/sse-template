package com.sseevents.template.controller;

import com.sseevents.template.service.SseEmitterService;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalTime;

@RestController
@RequestMapping("/emitter")
public class SseEmitterController {
    private SseEmitterService sseEmitterService;

    public SseEmitterController(SseEmitterService sseEmitterService) {
        this.sseEmitterService = sseEmitterService;
    }

    @GetMapping
    public Flux<ServerSentEvent<String>> emit() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> ServerSentEvent.<String> builder()
                        .id(String.valueOf(sequence))
                        .event("periodic-event")
                        .data("SSE - " + LocalTime.now().toString())
                        .build());
    }
}
