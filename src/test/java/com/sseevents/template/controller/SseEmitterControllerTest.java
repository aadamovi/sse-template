package com.sseevents.template.controller;

import com.sseevents.template.service.SseEmitterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SseEmitterController.class)
class SseEmitterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SseEmitterService dataSetService;

    @Test
    public void shouldEmitEvents() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/emitter"))
                .andExpect(request().asyncStarted())
                .andDo(MockMvcResultHandlers.log())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andDo(MockMvcResultHandlers.log())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/event-stream"));

        String event = mvcResult.getResponse().getContentAsString();
        System.out.println(event);
    }

}