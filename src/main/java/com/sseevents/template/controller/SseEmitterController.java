package com.sseevents.template.controller;

import com.sseevents.template.service.SseEmitterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/emitter")
public class SseEmitterController {

    private SseEmitterService sseEmitterService;
    private ExecutorService nonBlockingService = Executors.newCachedThreadPool();

    public SseEmitterController(SseEmitterService sseEmitterService) {
        this.sseEmitterService = sseEmitterService;
    }

    @GetMapping
    public SseEmitter emit() {
        SseEmitter emitter = new SseEmitter();
        nonBlockingService.execute(() -> {
            try {
                emitter.send("/sse" + " @ " + new Date());
                // we could send more events
                emitter.complete();
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });
        return emitter;
    }
}
