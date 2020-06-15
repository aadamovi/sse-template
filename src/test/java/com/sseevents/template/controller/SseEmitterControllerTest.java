package com.sseevents.template.controller;

import com.sseevents.template.service.SseEmitterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

@WebFluxTest(SseEmitterController.class)
class SseEmitterControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private SseEmitterService dataSetService;

    @Test
    public void shouldEmitFluxSse() {
        List<String> res = webClient.get()
                .uri("/emitter")
                .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
                .exchange()
                .returnResult(String.class)
                .getResponseBody()
                .take(3) // take 3 comment objects
                .collectList()
                .block();

        assertThat(res, notNullValue());
        assertThat(res.size(), is(3));
    }
}