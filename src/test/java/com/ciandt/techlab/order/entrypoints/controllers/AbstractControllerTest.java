package com.ciandt.techlab.order.entrypoints.controllers;

import com.ciandt.techlab.order.configurations.exceptions.ControllerExceptionHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.io.IOException;

public abstract class AbstractControllerTest {

    protected MockMvc mockMvc;

    protected void setUp(Object controller) {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(ControllerExceptionHandler.class)
                .setValidator(new LocalValidatorFactoryBean())
                .build();
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, clazz);
    }

    protected MvcResult mockMvcPost(String urlTemplate, Object content) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(mapToJson(content)))
                .andReturn();
    }

    protected MvcResult mockMvcPost(String urlTemplate) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    protected MvcResult mockMvcPut(String urlTemplate, Object content) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.put(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(mapToJson(content)))
                .andReturn();
    }

    protected MvcResult mockMvcPatch(String urlTemplate, Object content) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.patch(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(mapToJson(content)))
                .andReturn();
    }

    protected MvcResult mockMvcGet(String urlTemplate) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(urlTemplate)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
    }
}